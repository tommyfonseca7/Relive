package com.example.s.nav.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.R
import com.example.s.dataStructure.GenerateStatistics
import com.example.s.dataStructure.Post
import com.example.s.dataStructure.Stat
import com.example.s.nav.Screens
import kotlin.random.Random

@Composable
fun MemoryDetail(navController: NavController, modifier: Modifier =
    Modifier, p : Stat
){
    if (p.p?.matchId?.toInt() != -1){
        var s1 = GenerateStatistics()
        var s2 = GenerateStatistics()
        var random = Random
        val ballPossesionHome = random.nextInt(0,50)
        val ballPossesionAway = 100 - ballPossesionHome
        s1.ballPossession = ballPossesionHome
        s2.ballPossession = ballPossesionAway
        Column {
            Card(modifier = Modifier
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
                Column {
                    Box (modifier =  Modifier.clickable(onClick = { ->
                        p.p?.docId?.let {
                            navController.navigate(Screens.Gallery.route
                                .replace(
                                    oldValue = "{gameName}",
                                    newValue = it
                                ))
                        }
                    })){
                        if (p.p?.images?.size == 0){
                            Image(painter = painterResource(id = R.drawable.white),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .alpha(0.5f))
                            Text(text = "There is no images\n" +
                                    "Click here to upload your memories",
                                fontSize = 30.sp,
                                modifier = Modifier.align(Alignment.Center))
                        }else{
                            AsyncImage(model = p.p?.images?.get(0),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .alpha(0.5f)
                            )
                        }

                    }
                    Row (modifier = Modifier
                        .align(Alignment.CenterHorizontally)){
                        AsyncImage(
                            model = p.p?.homeTeamCrest,
                            contentScale = ContentScale.Crop,
                            contentDescription = "home team crest",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(20.dp))
                        Text(text = "${p.p?.homeScore.toString()}  -  ${p.p?.awayScore.toString()}",
                            fontSize = 50.sp,
                            modifier = Modifier
                                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        )
                        AsyncImage(
                            model = p.p?.awayTeamCrest,
                            contentScale = ContentScale.Crop,
                            contentDescription = "away team crest",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(20.dp))
                    }
                    p.p?.leagueName?.let {
                        Text(text = it,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally))
                    }
                    if ( p.p?.venue != null && p.p?.country != null){
                        Text(text = p.p!!.venue!! + ", " + p.p!!.country,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally))
                    }

                }
            }
            Column (modifier = Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())){
                ShowStatics(s1.ballPossession,s2.ballPossession,"ballPossession")
                ShowStatics(s1.cornerKicks, s2.cornerKicks, "corner Kicks")
                ShowStatics(s1.freeKicks,s2.freeKicks,"free Kicks" )
                ShowStatics(s1.goalKicks,s2.goalKicks, "Goal Kicks")
                ShowStatics(s1.shots,s2.shots, "Shots")
                ShowStatics(s1.saves,s2.saves, "Saves")
                ShowStatics(s1.shotsOnGoal,s2.shotsOnGoal, "Shot On Goal")
                ShowStatics(s1.shotsOffGoal, s2.shotsOffGoal, "Shot Off Goal")
                ShowStatics(s1.yellowCards,s2.yellowCards, "Yellow Card")
                ShowStatics(s1.redCards,s2.redCards, "Red Card")
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
            }
        }
    }else{
        Column {
            Card(modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxSize()
                .clickable(onClick = { ->
                    //onclick function
                }),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                ),
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Box (modifier =  Modifier.clickable(onClick = { ->
                        p.p?.docId?.let {
                            navController.navigate(Screens.Gallery.route
                                .replace(
                                    oldValue = "{gameName}",
                                    newValue = it
                                ))
                        }
                    })){
                        if (p.p?.images?.size == 0){
                            Image(painter = painterResource(id = R.drawable.white),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(500.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .alpha(0.5f))
                            Text(text = "There is no images\nClick here to upload your memories",
                                fontSize = 30.sp,
                                modifier = Modifier.align(Alignment.Center))
                        }else{
                            AsyncImage(model = p.p?.images?.get(0),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(500.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .alpha(0.5f)
                            )
                        }

                    }
                    Image(painter = painterResource(id = R.drawable.fcul), contentDescription ="fcul logotipo" ,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(100.dp))
                        Text(text = "CM Project Open Demo",
                            fontSize = 40.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally))

                        Text(text = "C6.3.27",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally))
                    Text(text = "Faculdade de Ciencias, Lisboa",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally))

                }
            }}
    }


}

@Composable
fun ShowStatics(home:Int,away:Int, title:String){
    val sum = home + away
    val frag = home.toFloat()/ sum
    var c1:Color
    var c2:Color
    if (home >= away){
        c1 = Color.Red
        c2 = Color.Gray
    }else{
        c2 = Color.Red
        c1 = Color.Gray
    }
    Column {
        Text(text = title,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Row (modifier = Modifier.fillMaxWidth()){
            Text(text = home.toString(), fontSize = 20.sp)
            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterVertically)){
                Image(painter = ColorPainter(c1),
                    contentDescription = "",
                    modifier = Modifier
                        .height(3.dp)
                        .fillMaxWidth(frag)
                        .align(Alignment.CenterEnd))

            }
            Box(modifier = Modifier
                .fillMaxWidth(0.80f)
                .align(Alignment.CenterVertically)){
                Image(painter = ColorPainter(c2),
                    contentDescription = "",
                    modifier = Modifier
                        .height(3.dp)
                        .fillMaxWidth(1 - frag)
                        .align(Alignment.CenterStart)
                        .padding(start = 1.dp))

            }
            Text(text = away.toString(), fontSize = 20.sp)
        }
    }
}