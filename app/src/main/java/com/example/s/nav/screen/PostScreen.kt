package com.example.s.nav.screen

import android.icu.text.DateFormat
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import coil.compose.AsyncImage
import com.example.s.R
import com.example.s.User
import com.example.s.dataStructure.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun PostScreen(/*navController: NavController, main: Activity, modifier: Modifier =
    Modifier, sportType: String, gameName: String*/
) {
    var postList by remember {
        mutableStateOf<List<Post>>(ArrayList())
    }
    val docRef = Firebase.firestore.collection("Users").document(Firebase.auth.currentUser?.uid!!)
    docRef.get().addOnSuccessListener { document ->
        if (document != null) {

            var user = document.toObject(User::class.java)
            Log.d("fdas",user?.email.toString()!!)
            if (user != null){
                Log.d("fdaeqws","dsewqa")
                var db = Firebase.database
                db.reference.child("memories").get().addOnSuccessListener { list ->
                    for (item in list.children){
                        Log.d("fdas","dsa")
                        var cast = item.getValue(Post::class.java)
                        if (cast != null && (cast.userUID == Firebase.auth.currentUser?.uid || user.friends.contains(cast.userUID))){
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

    ListALl(postList = postList)
}

@Composable
fun ListALl(postList: List<Post>){

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    {
        items(postList){ p ->
            PostListItem(p = p)
        }
    }
}

@Composable
fun PostListItem(p: Post){

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = { ->
                //onclick function
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
                            model = p.homeCrest,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .wrapContentHeight()
                        )
                        Text(text = p.homeScore.toString()
                            , fontSize = 30.sp)
                    }
                    Row {
                        AsyncImage(
                            model = p.awayCrest,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .wrapContentHeight()
                        )
                        Text(text = p.awayScore.toString(),
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

                    Text(
                        text = DateFormat.getPatternInstance(DateFormat.ABBR_MONTH_DAY).format(p.date),
                        fontSize = 30.sp,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }

            }

            //postimages(p = p)
        }

}

@Composable
private fun postimages(p : Post){
    if (p.images != null)
    LazyRow(){
        items(p.images){ id ->
            Image(painter = painterResource(id = id),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    //.size(84.dp)
                    .requiredHeight(150.dp)
                    .requiredWidth(340.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp))))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}