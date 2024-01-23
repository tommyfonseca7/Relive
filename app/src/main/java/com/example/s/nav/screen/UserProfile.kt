package com.example.s.nav.screen

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.s.R
import com.example.s.User
import com.example.s.nav.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import io.ktor.utils.io.bits.Memory
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun UserProfile(navController: NavController, main: Activity, modifier: Modifier = Modifier) {
    val auth = com.google.firebase.ktx.Firebase.auth
    val email = auth.currentUser?.email
    val coroutineScope = rememberCoroutineScope()
    val userDocumentId = remember { mutableStateOf<String?>(null) }
    val userData = remember { mutableStateOf<User?>(null) }


    LaunchedEffect(email) {
        if (email != null) {
            // Chama a função suspensa para obter o ID do documento baseado no email
            val documentId = getDocumentIdByEmail(email)
            if (documentId != null) {
                // Depois de obter o ID do documento, chama outra função suspensa para buscar os dados do usuário
                val userRef = FirebaseFirestore.getInstance().collection("Users").document(documentId)
                val documentSnapshot = userRef.get().await() // assume que você está usando a biblioteca 'kotlinx-coroutines-play-services'
                val user = documentSnapshot.toObject(User::class.java)
                userData.value = user
            }
        }
    }

    // Exibir detalhes do perfil
    userData.value?.let { user ->
        DisplayProfileDetails(user,navController)
    }

}

@Composable
fun DisplayProfileDetails(user: User,navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        TopSection(user = user, navController = navController ) // Para a parte superior do perfil
        StatsSection(user = user) // Para a seção de estatísticas
        //MemoriesSection(memories = user.memories) // Para a seção de memórias
    }
}

@Composable
fun TopSection(user: User, navController: NavController) {
    val auth = com.google.firebase.ktx.Firebase.auth
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                // Assumindo que você tem uma função para fazer o upload e obter a URL da imagem
                val imageUrl = uploadImageAndGetUrl(it)
                updateUserProfileImageUrl(user, imageUrl.toString())
                user.profileImageUrl =
                    imageUrl.toString()
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagem de perfil
        Image(
            painter = if (user.profileImageUrl.isNullOrEmpty()) {
                painterResource(id = R.drawable.ic_profile)
            } else {
                rememberAsyncImagePainter(model = user.profileImageUrl)
            },
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") }
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Nome e username
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            // Outros detalhes como 'Edit', 'Discover', etc.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val registrationDate = user.registrationDate?.toDate()?.let { date ->
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    formatter.format(date)
                } ?: "Data desconhecida"
                Column {
                    Text(
                        text = "Member since",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = registrationDate,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                val numberOfFriends = (user.friends as? List<*>)?.size ?: 0
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$numberOfFriends",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Friends ",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Row {
                TextButton(onClick = {
                    auth.signOut()
                    navController.navigate(Screens.Sign.route)
                }) {
                    Text("Log out", fontSize = 24.sp)
                }
            }
        }
    }
}

suspend fun uploadImageAndGetUrl(uri: Uri): String {
    val storageRef = Firebase.storage.reference
    val user = Firebase.auth.currentUser
    val imageRef = storageRef.child("profile_images/${user!!.uid}.jpg")

    return try {
        // Fazendo o upload da imagem
        imageRef.putFile(uri).await()
        // Obtendo a URL da imagem
        imageRef.downloadUrl.await().toString()
    } catch (e: Exception) {
        // Tratamento de erros
        Log.e("uploadImage", "Falha no upload da imagem", e)
        ""
    }
}

suspend fun updateUserProfileImageUrl(user: User, imageUrl: String) {
    val documentId = getDocumentIdByEmail(user.email)
    val userRef = documentId?.let { FirebaseFirestore.getInstance().collection("Users").document(it) }
    userRef?.update("profileImageUrl", imageUrl)?.addOnSuccessListener {
        // Sucesso na atualização
        Log.d("UpdateProfileImage", "Imagem de perfil atualizada com sucesso.")
    }?.addOnFailureListener { e ->
        // Falha na atualização
        Log.e("UpdateProfileImage", "Erro ao atualizar a imagem de perfil.", e)
    }
}

@Composable
fun StatsSection(user: User) {
    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Games",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Text(
                text = "Estádios",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Text(
                text = "Equipas",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            Text(
                text = "Hrs:Mins",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Composable
fun MemoriesSection(memories: List<Memory>) {

}

