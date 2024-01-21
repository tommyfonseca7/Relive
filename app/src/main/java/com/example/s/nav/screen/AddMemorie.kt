package com.example.s.nav.screen

import android.app.Activity
import android.nfc.Tag
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.s.User
import com.example.s.network.MatchInfo
import com.example.s.network.MatchesApi
import com.example.s.utils.EditField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


enum class FootballLeague {
    WORLD_CUP,
    CHAMPIONS_LEAGUE,
    BUNDESLIGA,
    EREDIVISIE,
    CAMPEONATO_BRASILEIRO,
    PRIMERA_DIVISION,
    LIGUE_1,
    CHAMPIONSHIP,
    PRIMEIRA_LIGA,
    EUROPEAN_CHAMPIONSHIP,
    SERIE_A,
    PREMIER_LEAGUE,
    COPA_LIBERTADORES,
    OTHER
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemorie(navController: NavController, main: Activity, modifier: Modifier =
    Modifier) {

    var selectedLeague by remember { mutableStateOf<FootballLeague?>(null) }
    var otherLeague by remember { mutableStateOf("") }
    var homeClub by remember { mutableStateOf("") }
    var awayClub by remember { mutableStateOf("") }
    val leagues = FootballLeague.values()
    var expanded by remember { mutableStateOf(false) }
    var matches by remember { mutableStateOf<List<MatchInfo>?>(null) }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis <= System.currentTimeMillis()
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    }

    var searchUsername by remember { mutableStateOf("") }
    var foundUser by remember { mutableStateOf<User?>(null) }
    var color by remember { mutableStateOf(Color.LightGray) }
    val userRef = FirebaseFirestore.getInstance().collection("Users")
    val coroutineScope = rememberCoroutineScope()
    var auth = Firebase.auth
    var email = auth.currentUser?.email
    val userDocumentId = remember { mutableStateOf<String?>(null) }
    var competitionCode by remember { mutableStateOf("") }

    var selectedMatch by remember { mutableStateOf<MatchInfo?>(null) }
    var expandedMatch by remember { mutableStateOf(false) }


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
            text = "Add Memory",
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
    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(top = 65.dp)
            .verticalScroll(rememberScrollState()),
    ) {

        Box {
            TextButton(onClick = { expanded = true }) {
                Text(text = selectedLeague?.name?.replace("_", " ")
                    ?.split(" ")?.joinToString(" ") { it.lowercase().capitalize() }
                    ?: "Select a League"
                )
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select a League", tint = Color(0xFF2462C2))
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false},
                modifier = Modifier.background(Color.White)
            ) {
                leagues.forEach { league ->
                    if (league != FootballLeague.OTHER) {
                        DropdownMenuItem(
                            text = { Text(text = league.name.replace("_", " ").split(" ").joinToString(" ") { it.lowercase().capitalize() }) },
                            onClick = {
                                selectedLeague = league
                                expanded = false
                                competitionCode = when (selectedLeague) {
                                    FootballLeague.WORLD_CUP -> "WC"
                                    FootballLeague.CHAMPIONS_LEAGUE -> "CL"
                                    FootballLeague.BUNDESLIGA -> "BL1"
                                    FootballLeague.EREDIVISIE -> "DED"
                                    FootballLeague.CAMPEONATO_BRASILEIRO -> "BSA"
                                    FootballLeague.PRIMERA_DIVISION -> "PD"
                                    FootballLeague.LIGUE_1 -> "FL1"
                                    FootballLeague.CHAMPIONSHIP -> "ELC"
                                    FootballLeague.PRIMEIRA_LIGA -> "PPL"
                                    FootballLeague.EUROPEAN_CHAMPIONSHIP -> "EC"
                                    FootballLeague.SERIE_A -> "SA"
                                    FootballLeague.PREMIER_LEAGUE -> "PL"
                                    FootballLeague.COPA_LIBERTADORES -> "CLI"
                                    else -> ""
                                }
                            }
                        )
                    }
                }
                DropdownMenuItem(
                    text = { Text(text = "Other")},
                    onClick = {
                        selectedLeague = FootballLeague.OTHER
                        expanded = false

                    }
                )
            }
        }




        Spacer(modifier = Modifier.height(16.dp))

        if (selectedLeague == FootballLeague.OTHER) {
            EditField(
                label = "Enter other league",
                value = otherLeague,
                onValueChanged = {otherLeague = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                )

            EditField(
                label = "Select Home Club",
                value = homeClub,
                onValueChanged = {homeClub = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            EditField(
                label = "Enter Away Club",
                value = awayClub,
                onValueChanged = {awayClub = it},
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                color = color,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        DatePicker(state = datePickerState)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                try {
                    Log.d("Api Request", "URL: ${MatchesApi.retrofitService.getMatches(competitionCode,selectedDate.toString(), selectedDate.toString()).toString()}")
                    matches = MatchesApi.retrofitService.getMatches(competitionCode,selectedDate.toString(), selectedDate.toString()).matches

                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }) {
            Text(text = "Search matches")
        }



        Box {
            TextButton(onClick = { expandedMatch = true }) {
                selectedMatch?.let { match ->
                    Text(
                        text = "${match.homeTeam.name} vs ${match.awayTeam.name}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select a League", tint = Color(0xFF2462C2))
            }

            DropdownMenu(
                expanded = expandedMatch,
                onDismissRequest = { expandedMatch = false },
                modifier = Modifier.background(Color.White)
            ) {
                // Iterate over matches and add them to the dropdown
                matches?.forEach { match ->
                    DropdownMenuItem(
                        onClick = {
                            selectedMatch = match
                            expandedMatch = false
                        }, text = { Text(text = "${match.homeTeam.name} vs ${match.awayTeam.name}") }
                    )
                }
            }
        }

        Button(onClick = {
            // Handle the button click and the selected league or otherLeague value
            val selectedLeagueValue = selectedLeague ?: FootballLeague.OTHER
            val leagueValue = if (selectedLeagueValue == FootballLeague.OTHER) otherLeague else selectedLeagueValue.name
            // Perform further actions with the selected league value
        }) {
            Text(text = "Add Football Memory")
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date(millis))
}







