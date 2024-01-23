package com.example.s.nav.screen

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.s.R
import com.example.s.User
import com.example.s.dataStructure.Post
import com.example.s.dataStructure.Stat
import com.example.s.nav.Screens
import com.example.s.network.MatchesApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.AbstractCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PostScreen(navController: NavController, p:Stat
) {
    var postList by remember {
        mutableStateOf<List<Post>>(ArrayList())
    }
    Log.d("cast","dssaddas")
    val db =Firebase.firestore
    val docRef = db.collection("Users").document(Firebase.auth.currentUser?.uid!!)
    docRef.get().addOnSuccessListener { document ->
        if (document != null) {

            var user = document.toObject(User::class.java)
            if (user != null){
                db.collection("Memories").get().addOnSuccessListener { list ->
                    for (item in list){
                        var cast = item.toObject(Post::class.java)

                        if (cast != null && (cast.userId == Firebase.auth.currentUser?.uid || user.friends.contains(cast.userId))){
                            Log.d("ids", cast.matchId.toString())
                            if (!postList.contains(cast)){
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
                    Image(painter = painterResource(id = R.drawable.pic2),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .alpha(0.5f))

                    p.date?.let {
                        Text(
                            text = it,
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }

            }
        }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}