package com.example.s.nav.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.s.User
import com.example.s.dataStructure.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(/*navController: NavController, main: Activity, modifier: Modifier =
    Modifier, sportType: String, gameName: String*/
) {
    val ps = remember {
        //DataProvider.post
    }
    val db = Firebase.firestore
    var li = emptyList<String>()
    val docRef = db.collection("Users").document("FDSEUTOme7OsJL2Fea8f")
    docRef.get().addOnSuccessListener { document ->
        if (document != null) {
            var user = document.toObject(User::class.java)
            for (f in user?.friends!!){
                Log.d("friend", f)
            }
        }
    }

    val pageState = rememberPagerState(pageCount = { 2 })

    Box(modifier = Modifier.fillMaxSize()){
        HorizontalPager(
            state = pageState
        ) { index ->
            if (ps.images != null){
                Image(painter = painterResource(id = ps.images[index]),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }


        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageState.pageCount) { iteration ->
                val color = if (pageState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }


}*/