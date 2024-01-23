package com.example.s.nav.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.R
import com.example.s.dataStructure.GenerateStatistics
import com.example.s.dataStructure.Post
import com.example.s.dataStructure.Stat

@Composable
fun MemoryDetail(navController: NavController, modifier: Modifier =
    Modifier, p : Stat
){
    var s1 = GenerateStatistics()
    var s2 = GenerateStatistics()
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
                Image(painter = painterResource(id = R.drawable.pic2),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .alpha(0.5f))
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
            ShowStatics(s1.fouls, s2.fouls,"fouls")
            ShowStatics(s1.freeKicks,s2.freeKicks,"free Kicks" )
            ShowStatics(s1.goalKicks,s2.goalKicks, "Goal Kicks")
            ShowStatics(s1.shots,s2.shots, "Shots")
            ShowStatics(s1.saves,s2.saves, "Saves")
            ShowStatics(s1.shotsOnGoal,s2.shotsOnGoal, "Shot On Goal")
            ShowStatics(s1.shotsOffGoal, s2.shotsOffGoal, "Shot Off Goal")
            ShowStatics(s1.offsides,s2.offsides,"Off Sides")
            ShowStatics(s1.throwIns,s2.throwIns, "Throw Ins")
            ShowStatics(s1.yellowRedCards,s2.yellowRedCards, "Yellow Red Card")
            ShowStatics(s1.yellowCards,s2.yellowCards, "Yellow Card")
            ShowStatics(s1.redCards,s2.redCards, "Red Card")
        }
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

                Divider(color = c1,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(frag)
                        .align(Alignment.CenterEnd))
            }
            Box(modifier = Modifier
                .fillMaxWidth(0.80f)
                .align(Alignment.CenterVertically)){
                Divider(color = c2,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(1-frag)
                        .align(Alignment.CenterStart)
                        .padding(start = 1.dp))

            }
            Text(text = away.toString(), fontSize = 20.sp)
        }

    }
}