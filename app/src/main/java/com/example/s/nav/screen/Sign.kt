package com.example.s.nav.screen

import android.app.Activity
import android.graphics.fonts.FontFamily
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.nav.Screens
import com.example.s.utils.EditField
import com.example.s.utils.EditPassField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@Composable
fun SignIn(navController: NavController, main: Activity, modifier: Modifier =
    Modifier
        .fillMaxSize()
        .wrapContentSize(
            Alignment.Center
        )) {
    var auth = Firebase.auth
    Log.d("1323", (auth.currentUser == null).toString())
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var errm by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(LightGray) }
    Column(
        modifier = modifier,
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

        Text(text = errm,
            color = Color.Red)

        ClickableText(
            text = AnnotatedString("Register") ,
            onClick = {
                navController.navigate(Screens.Regist.route)
            },
            style = TextStyle(
                color = Blue,
                fontSize = 20.sp
            )
        )
        
        Button(onClick = {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(main) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        navController.navigate(Screens.Done.route)
                    } else {
                        color = Red
                        errm = "Your email or password is wrong"
                    }
                }

                         },
            modifier = Modifier.padding(top = 10.dp)) {
            Text("Log in", fontSize = 24.sp)
        }
    }


}