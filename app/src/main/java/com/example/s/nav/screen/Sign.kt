package com.example.s.nav.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.fonts.FontFamily
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.s.MainActivity
import com.example.s.nav.Screens
import com.example.s.utils.EditField
import com.example.s.utils.EditPassField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



@Composable
fun SignIn(navController: NavController, main: MainActivity) {
    var auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var errm by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(LightGray) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "Sign In",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.TopStart),
            style = TextStyle(color = Color(0xFF2462C2),
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
                label = "Email",
                value = email,
                onValueChanged = { email = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            EditPassField(
                label = "Password",
                value = pass,
                onValueChanged = { pass = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
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
                    if (email.isEmpty() || pass.isEmpty()) {
                        errm = "Email or Password are blank, fill them and try again"
                    } else {
                        auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(main) { task ->
                                if (task.isSuccessful) {
                                    navController.navigate(Screens.MemoriesScreen.route)
                                } else {
                                    color = Red
                                    errm = "Your email or password is wrong"
                                }
                            }
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Log in", fontSize = 24.sp)
            }

            Button(
                onClick = {
                    main.changeToPhoto()
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Photo", fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ClickableText(
                text = AnnotatedString("Don't have an account? Register here."),
                onClick = {
                    navController.navigate(Screens.Regist.route)
                },
                style = TextStyle(
                    color = Blue,
                    fontSize = 15.sp
                )
            )
        }
    }
}
