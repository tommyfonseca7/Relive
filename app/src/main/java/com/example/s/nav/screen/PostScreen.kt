package com.example.s.nav.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.s.R
import com.example.s.dataStructure.Post
import com.example.s.dataStructure.Stat
import com.example.s.dataStructure.User
import com.example.s.nav.Screens
import com.example.s.network.MatchesApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun PostScreen(navController: NavController, p:Stat
) {
    var postList by remember {
        mutableStateOf<List<Post>>(ArrayList())
    }
    var checkList by remember {
        mutableStateOf<ArrayList<String>>(ArrayList())
    }
    val db =Firebase.firestore
    if (Firebase.auth.currentUser != null){
        val docRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {

                var user = document.toObject(User::class.java)
                if (user != null){

                    db.collection("Memories").get().addOnSuccessListener { list ->
                        for (item in list){
                            var cast = item.toObject(Post::class.java)
                            if (cast != null && (cast.userId == Firebase.auth.currentUser?.uid || user.friends.contains(cast.userId))){
                                if (!checkList.contains(item.id)){
                                    checkList.add(item.id)
                                    cast.docId = item.id
                                    postList = postList.toMutableList().apply {
                                        add(cast) }
                                }
                            }
                        }
                    }
                }

            }
        }

        ListALl(navController,postList = postList, p)
    }

}

@Composable
fun ListALl(navController: NavController,postList: List<Post>, stat:Stat){
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        items(postList){ p ->
            PostListItem(navController = navController,p = p, coroutineScope = coroutineScope, stat = stat)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PostListItem(navController: NavController,p: Post, coroutineScope: CoroutineScope, stat:Stat){
    var homeScore by remember {
        mutableStateOf(0)
    }
    var awayScore by remember {
        mutableStateOf(0)
    }
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(p.userId) {
        user = getUserById(p.userId.toString())
    }
    if (p.matchId?.toInt() != -1){
        coroutineScope.launch {
            try {
                var result = MatchesApi.retrofitService.getInf(p.matchId.toString())
                homeScore = result.scores.Score.homeScore
                awayScore = result.scores.Score.awayScore
                p.leagueName = result.league.name
                p.venue = result.venue
                p.country = result.country.name
            }catch (e :Exception){

            }
        }
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable(onClick = { ->
                    //onclick function
                    p.awayScore = awayScore
                    p.homeScore = homeScore

                    stat.p = p
                    navController.navigate(Screens.Detail.route)
                }),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Row(modifier = Modifier.padding(bottom = 10.dp)) {

                        AsyncImage(
                            model = p.homeTeamCrest,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .wrapContentHeight()
                        )
                        Text(text = homeScore.toString()
                            , fontSize = 30.sp)
                    }
                    Row {
                        AsyncImage(
                            model = p.awayTeamCrest,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .wrapContentHeight()
                        )
                        Text(text = awayScore.toString(),
                            fontSize = 30.sp)
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()){
                    if (p.images?.size == 0){
                        Image(painter = painterResource(id = R.drawable.white),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .alpha(0.5f)
                        )
                    }else{
                        AsyncImage(model = p.images?.get(0),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .alpha(0.5f)
                        )
                    }


                    p.date?.let {
                        // Text aligned to the top end
                        Text(
                            text = it,
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }

                    user?.let {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // Isso alinha a Row no canto inferior direito do Box
                                .padding(16.dp) // Isso adiciona um espaçamento ao redor da Row
                        ) {
                            Image(
                                painter = if (user!!.profileImageUrl.isNullOrEmpty()) {
                                    painterResource(id = R.drawable.ic_profile)
                                } else {
                                    rememberAsyncImagePainter(model = user!!.profileImageUrl)
                                },
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "@${user!!.username}",
                                fontSize = 20.sp,


                                )
                        }
                    }

                }

            }
        }
    }else{
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable(onClick = { ->
                    stat.p = p
                    navController.navigate(Screens.Detail.route)
                }),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Row(modifier = Modifier.padding(bottom = 10.dp)) {

                        Image(painter = painterResource(id = R.drawable.fcul), contentDescription ="fcul logotipo" )
                    }

                }

                Box(modifier = Modifier.fillMaxWidth()){
                    if (p.images?.size == 0){
                        Image(painter = painterResource(id = R.drawable.white),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .alpha(0.5f)
                        )
                    }else{
                        AsyncImage(model = p.images?.get(0),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .alpha(0.5f)
                        )
                    }
                    p.date?.let {
                        // Text aligned to the top end
                        Text(
                            text = it,
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }

                    user?.let {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // Isso alinha a Row no canto inferior direito do Box
                                .padding(16.dp) // Isso adiciona um espaçamento ao redor da Row
                        ) {
                            Image(
                                painter = if (user!!.profileImageUrl.isNullOrEmpty()) {
                                    painterResource(id = R.drawable.ic_profile)
                                } else {
                                    rememberAsyncImagePainter(model = user!!.profileImageUrl)
                                },
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "@${user!!.username}",
                                fontSize = 20.sp,
                                )
                        }
                    }

                }

            }
        }
    }


}

private suspend fun getUserById(userId : String) : User? {
    val userRef = FirebaseFirestore.getInstance().collection("Users")
    val user = userRef
        .document(userId)
        .get()
        .await()
        .toObject(User::class.java)

    return user

}

@Composable
fun PostScreenForMe(navController: NavController, p:Stat
) {
    var postList by remember {
        mutableStateOf<List<Post>>(ArrayList())
    }
    var checkList by remember {
        mutableStateOf<ArrayList<String>>(ArrayList())
    }
    val db =Firebase.firestore
    if (Firebase.auth.currentUser != null){
        val docRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {

                var user = document.toObject(User::class.java)
                if (user != null){

                    db.collection("Memories").get().addOnSuccessListener { list ->
                        for (item in list){
                            var cast = item.toObject(Post::class.java)
                            if (cast != null && (cast.userId == Firebase.auth.currentUser?.uid )){
                                if (!checkList.contains(item.id)){
                                    checkList.add(item.id)
                                    cast.docId = item.id
                                    postList = postList.toMutableList().apply {
                                        add(cast) }
                                }
                            }
                        }
                    }
                }

            }
        }

        ListALl(navController,postList = postList, p)
    }

}