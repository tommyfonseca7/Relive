package com.example.s.nav.screen

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

    @Composable
    fun GalleryScreen(navController: NavController, main: Activity, modifier: Modifier =
        Modifier, sportType: String, gameName: String
    ) {
        var imageUris by remember {
            mutableStateOf<List<Uri>>(ArrayList())
        }
        var storage = Firebase.storage
        val user = Firebase.auth.currentUser
        Log.d("images", user.toString())
        val multiplePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = {
                for (uri in it){
                    val storageReference = Firebase.storage
                        .getReference(user!!.uid)
                        .child(sportType)
                        .child(gameName)
                        .child(uri.lastPathSegment!!)
                    putImageInStorage(storageReference, uri,"")
                    imageUris = imageUris.toMutableList().apply {
                        add(uri) }
                }
            }
        )
            storage.getReference(user!!.uid).child(sportType).child(gameName).listAll().addOnSuccessListener { items ->
                for (item in items.items) {
                    item.downloadUrl.addOnSuccessListener { uri ->
                        if (!imageUris.contains(uri)){
                            imageUris = imageUris.toMutableList().apply {
                                add(uri) }
                        }

                    }
                }

            }

        Column {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Gallery",
                        modifier = Modifier.padding(bottom = 16.dp),
                        style = TextStyle(
                            color = Color(0xFF2462C2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                    )
                }
                Button(onClick = {
                    multiplePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                }) {
                    Text(text = "Upload Image")
                }
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(200.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(imageUris) { photo ->
                        AsyncImage(
                            model = photo,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        //UI


    }

    private fun load(sportType : String, gameName : String, uris: List<Uri>){
        val user = Firebase.auth.currentUser
        var imageUris = uris
        Firebase.storage.getReference(user!!.uid).child(sportType).child(gameName).listAll().addOnSuccessListener { items ->
            /*items.prefixes.forEach { s ->
                Log.d("images", s.name.toString())
            }*/
            for (item in items.items) {
                item.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("image",uri.toString())
                    imageUris = imageUris.toMutableList().apply {
                        add(uri) }
                }
            }

        }
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        storageReference.putFile(uri)
            .addOnSuccessListener() { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // on success
            }
            .addOnFailureListener() { e ->
                Log.w(
                    "image save failed",
                    "Image upload task was unsuccessful.",
                    e
                )
            }
    }

