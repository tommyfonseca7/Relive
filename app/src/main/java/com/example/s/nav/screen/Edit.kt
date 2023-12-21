package com.example.s.nav.screen

import android.app.Activity
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.nav.Screens
import com.example.s.utils.EditField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Edit(navController: NavController, main: Activity, modifier: Modifier =
    Modifier
        .fillMaxSize()
        .wrapContentSize(
            Alignment.Center
        )) {
    var auth = Firebase.auth
    var data = Firebase.database
    var name by remember { mutableStateOf("") }
    Column (modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally){
        EditField(
            label = "Your name",
            value = name,
            onValueChanged = { name = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            color = Color.LightGray,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Button(onClick = {
            //auth.currentUser?.email?.let { data.getReference(it).setValue(name) }
            navController.navigate(Screens.Done.route)
        }) {
            Text("Save", fontSize = 24.sp)
        }
    }
}