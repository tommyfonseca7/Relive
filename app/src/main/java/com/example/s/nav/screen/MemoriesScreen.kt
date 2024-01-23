package com.example.s.nav.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.R
import com.example.s.User
import com.example.s.dataStructure.Post
import com.example.s.dataStructure.Stat
import com.example.s.nav.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


@Composable
fun MemoriesScreen(navController: NavController, main: Activity, modifier: Modifier =
    Modifier, p : Stat
) {

    var searchUsername by remember { mutableStateOf("") }
    var foundUser by remember { mutableStateOf<User?>(null) }
    var color by remember { mutableStateOf(Color.LightGray) }
    val userRef = FirebaseFirestore.getInstance().collection("Users")
    val coroutineScope = rememberCoroutineScope()
    var auth = Firebase.auth
    var email = auth.currentUser?.email
    var user = auth.currentUser
    val userDocumentId = remember { mutableStateOf<String?>(null) }
    Log.d("mem","das")
    LaunchedEffect(email) {
        userDocumentId.value = getDocumentIdByEmail(email)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Memories",
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp),
                style = TextStyle(
                    color = Color(0xFF2462C2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.End)
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = {
                        // Navegação para a tela de busca de usuários
                        navController.navigate(Screens.SearchUsers.route)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_group_add_24),
                        contentDescription = "add friend",
                        tint = Color(0xFF2462C2)
                    )
                }
            }
        }
    }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            PostScreen(navController,p=p)
            Button(onClick = {navController.navigate(Screens.AddMemorie.route)}) {
                Text(text = "Add Memory")
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Memorie", tint = Color.White)
            }
            Button(onClick = {navController.navigate(Screens.Gallery.route
                .replace(
                    oldValue = "{sportType}",
                    newValue = "Football")
                .replace(
                    oldValue = "{gameName}",
                    newValue = "test")
                // este parte serve para passar o tipo d edesporto e que jogo queremos ver
            )}) {
                Text(text = "Gallery")
            }
        }
    }










