package com.example.s.nav.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.R
import com.example.s.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
        DisplayProfileDetails(user)
    }

}

@Composable
fun DisplayProfileDetails(user: User) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Profile",
                style = TextStyle(
                    color = Color(0xFF2462C2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                val imageModifier = Modifier.size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)

                // Foto de perfil padrão
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Default Profile",
                    modifier = imageModifier
                )

                Text(
                        text = "${user.name}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "@${user.username}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                val numberOfFriends = (user.friends as? List<*>)?.size ?: 0
                Text(
                    text = "Friends: $numberOfFriends",
                    style = MaterialTheme.typography.bodyLarge
                )
                val registrationDate = user.registrationDate?.toDate()?.let { date ->
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    formatter.format(date)
                } ?: "Data desconhecida"
                Text(
                    text = "Member since: $registrationDate",
                    style = MaterialTheme.typography.bodySmall
                )
                }
            }
        }
}