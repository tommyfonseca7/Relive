package com.example.s.nav.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.nav.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Done(navController: NavController, modifier: Modifier =
    Modifier
        .fillMaxSize()
        .wrapContentSize(
            Alignment.Center
        )) {
    var auth = Firebase.auth
    var user = auth.currentUser?.email
    var db = Firebase.database
    var name by remember { mutableStateOf("") }

    var s = "Welcome user $user"
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            auth.signOut()
            navController.navigate(Screens.Sign.route)
        }) {
            Text("Log out", fontSize = 24.sp)
        }
    }
    Text(text = s, fontSize = 26.sp)


}