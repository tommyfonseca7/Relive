package com.example.s.nav.screen

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.ktx.Firebase

@Composable
fun Regist(navController: NavController, main: Activity) {
    var auth = Firebase.auth
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confpass by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.LightGray) }
    var errm by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {

        Text(
            text = "Register",
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

            EditPassField(
                label = "Confirm Password",
                value = confpass,
                onValueChanged = { confpass = it },
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
                        errm = "Email or Passwords are blank, fill them and try again."
                    } else if (!(pass.equals(confpass))) {
                        errm = "Passwords don't match, try again."
                    } else {
                        auth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(main) { task ->
                                if (task.isSuccessful) {
                                    navController.navigate(Screens.PostRegistForm.route)
                                } else {
                                    color = Color.Red
                                }
                            }
                    }

                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Register", fontSize = 24.sp)
            }
        }
    }
}
