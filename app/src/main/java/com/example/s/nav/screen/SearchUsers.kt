package com.example.s.nav.screen

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.R
import com.example.s.User
import com.example.s.nav.Screens
import com.example.s.utils.EditField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


@Composable
fun SearchUsers(navController: NavController, main: Activity, modifier: Modifier =
    Modifier
) {
    var searchUsername by remember { mutableStateOf("") }
    var foundUser by remember { mutableStateOf<User?>(null) }
    var color by remember { mutableStateOf(Color.LightGray) }
    val userRef = FirebaseFirestore.getInstance().collection("Users")
    val coroutineScope = rememberCoroutineScope()
    var auth = Firebase.auth
    var email = auth.currentUser?.email
    val userDocumentId = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(email) {
        userDocumentId.value = getDocumentIdByEmail(email)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "Add Friends",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.TopStart),
            style = TextStyle(
                color = Color(0xFF2462C2),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditField(
            label = "username",
            value = searchUsername,
            onValueChanged = { searchUsername = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            color = color,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Button(onClick = {
            if (searchUsername.isNotEmpty()) {
                coroutineScope.launch {
                    foundUser = withContext(Dispatchers.IO) {
                        try {
                            val querySnapshot = userRef
                                .whereEqualTo("username", searchUsername)
                                .limit(1)
                                .get()
                                .await()

                            if (!querySnapshot.isEmpty) {
                                querySnapshot.documents[0].toObject(User::class.java)
                            } else {
                                null
                            }
                        } catch (e : Exception) {
                            null
                        }
                    }
                }
            }
        }) {
            Text(text = "Search")
        }


        foundUser?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Name: ${user.name}")
                Text(text = "Username: ${user.username}")

                Button(onClick = {
                    coroutineScope.launch {
                        if (addUserAsfriend(user, currUserID = userDocumentId.value.toString())) {
                            Toast.makeText(main.baseContext, "User added", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(main.baseContext, "Error adding user", Toast.LENGTH_SHORT).show()
                        }
                    }

                }) {
                    Text(text = "Add as friend")
                }
            }
        }

    }

}
@Composable
private fun showShortToast(context: Context, message: String) {
    LaunchedEffect(Unit) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

suspend fun getDocumentIdByEmail(email: String?): String? {
    val collectionReference = FirebaseFirestore.getInstance().collection("Users")
    return try {
        val querySnapshot = collectionReference
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].id
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }

}


private suspend fun addUserAsfriend(user: User, currUserID: String) : Boolean{
    val collectionReference = FirebaseFirestore.getInstance().collection("Users")

    return try {
        collectionReference
            .document(currUserID)
            .update("friends", FieldValue.arrayUnion(getDocumentIdByEmail(user.email)))
            .await()
        true

    } catch (e : Exception) {
        false
    }
}
