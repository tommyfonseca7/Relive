package com.example.s.nav.screen

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.nav.Screens
import com.example.s.utils.EditField
import com.example.s.utils.EditPassField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp

@Composable
fun PostRegistForm(navController: NavController, main: Activity, modifier: Modifier =
    Modifier
        .fillMaxSize()
        .wrapContentSize(
            Alignment.Center
        )) {
    var auth = Firebase.auth
    var email = auth.currentUser?.email
    var name by  remember { mutableStateOf("") }
    var errm by remember { mutableStateOf("") }
    var username by  remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.LightGray) }
    var sports by remember { mutableStateOf("") }
    val db = Firebase.firestore
    val usersRef = db.collection("Users")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "We're almost done...",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.TopStart),
            style = TextStyle(
                color = Color(0xFF2462C2),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditField(
                label = "Name",
                value = name,
                onValueChanged = { name = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            EditField(
                label = "Username",
                value = username,
                onValueChanged = { username = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            EditField(
                label = "Favourite Sports",
                value = sports,
                onValueChanged = { sports = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            Text(
                text = errm,
                color = Color.Red,
                fontSize = 15.sp
            )

            Button(
                onClick = {
                    val query = usersRef.whereEqualTo("username", username)
                    query.get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                val sportsArr = sports.split(", ")
                                val user = hashMapOf(
                                    "name" to name,
                                    "username" to username,
                                    "Sports" to sportsArr,
                                    "email" to email,
                                    "friends" to emptyList<String>(),
                                    "registrationDate" to Timestamp.now()
                                )
                                if (Firebase.auth.uid != null){
                                    usersRef.document(Firebase.auth.uid!!).set(user)
                                        .addOnSuccessListener { navController.navigate(Screens.MemoriesScreen.route) {
                                            popUpTo(Screens.Sign.route) {
                                                inclusive = true
                                            }
                                        }

                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error adding document", e)
                                        }
                                }

                            } else {
                                errm = "Username already in use, choose another."
                            }
                        }
                          },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Submit", fontSize = 24.sp)
            }


        }
    }

}