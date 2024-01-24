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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.s.dataStructure.Stat
import com.example.s.dataStructure.Post
import com.example.s.dataStructure.User
import com.example.s.nav.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.checkerframework.common.subtyping.qual.Bottom
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun UserProfile(
    navController: NavController,
    main: Activity,
    modifier: Modifier = Modifier,
    p: Stat
) {
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
        DisplayProfileDetails(user,navController,p, main)
    }

}

@Composable
fun DisplayProfileDetails(user: User, navController: NavController, p: Stat, main: Activity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        TopSection(user = user, navController = navController, p =p , main) // Para a parte superior do perfil
        StatsSection(user = user) // Para a seção de estatísticas
        MemoriesSection(navController,p) // Para a seção de memórias
    }
}

@Composable
fun TopSection(user: User, navController: NavController, p: Stat, main: Activity) {
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
                updateUserProfileImageUrl(user, imageUrl)
                user.profileImageUrl = imageUrl
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
            Row  (modifier = Modifier.fillMaxWidth()){
                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                    Text(
                        text = "@${user.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                val numberOfFriends = (user.friends as? List<*>)?.size ?: 0
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 30.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Friends ",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = "$numberOfFriends",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                }
            }

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

                TextButton(
                    onClick = {
                        p.signout = 1
                        navController.navigate(Screens.Sign.route)
                        main.finish()
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(20))
                        .height(48.dp)
                        .width(80.dp)
                        .background(Color(0xFF2462C2))


                ) {
                    Text(
                        "Log out",
                        fontSize = 16.sp,
                        color = Color.White,
                    )
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        var stadiums by remember { mutableStateOf(0) }
        var teams by remember { mutableStateOf(0) }
        LaunchedEffect(user.memories) {
            // Launch a coroutine to fetch the number of stadiums asynchronously
            stadiums = getNumberOfStadiums(user.memories)
            teams = getNumberOfTeams(user.memories)
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            val games = user.memories.size
            Log.d("StatsSection", "Number of games: $games")
            StatItem(label = "Games", number = games)
            Divider(color = Color.White, modifier = Modifier
                .height(60.dp)
                .width(1.dp))
            StatItem(label = "Stadiums", number = stadiums)
            Divider(color = Color.White, modifier = Modifier
                .height(60.dp)
                .width(1.dp))
            StatItem(label = "Teams", number = teams)
            Divider(color = Color.White, modifier = Modifier
                .height(60.dp)
                .width(1.dp))
            val totalMemoriesTime = user.memories.size * 90
            val hours = totalMemoriesTime / 60
            val minutes = totalMemoriesTime % 60
            StatItem(label = "Hrs:Mins", number = hours, number2 = minutes)
        }
    }
}

private suspend fun getNumberOfStadiums(memories : List<String>) : Int {
    val memoriesRef = FirebaseFirestore.getInstance().collection("Memories")
    var stadiums = mutableListOf<String>()
    for (memoryId in memories) {
        val memory = memoriesRef
            .document(memoryId)
            .get()
            .await()
            .toObject(Post::class.java)
        memory?.venue?.let {
            if (!stadiums.contains(it)){
                stadiums.add(it)
            }
        }
    }
    return stadiums.size
}

private suspend fun getNumberOfTeams(memories : List<String>) : Int {
    val memoriesRef = FirebaseFirestore.getInstance().collection("Memories")
    var teams = mutableListOf<String>()
    for (memoryId in memories) {
        val memory = memoriesRef
            .document(memoryId)
            .get()
            .await()
            .toObject(Post::class.java)
        memory?.let {
            if (!teams.contains(it.homeTeam)) {
                teams.add(it.homeTeam)
            }
            if (!teams.contains(it.awayTeam)) {
                teams.add(it.awayTeam)
            }
        }
    }

    return teams.size
}
fun formatTime(hours: Int, minutes: Int): String {
    return String.format("%02d:%02d", hours, minutes)
}
@Composable
fun StatItem(label: String, number: Int, number2: Int? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (number2 != null) formatTime(number, number2) else number.toString(),
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )
    }
}

@Composable
fun MemoriesSection(navController: NavController, p: Stat) {
    Log.d("ewq","eqw")
    PostScreenForMe(navController,p)
}

