package com.example.s.nav.screen

import android.Manifest
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.s.MainActivity
import com.example.s.dataStructure.User
import com.example.s.nav.Screens
import com.example.s.network.MatchInfo
import com.example.s.network.MatchesApi
import com.example.s.utils.EditField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
fun AddMemorie(navController: NavController, main: MainActivity, modifier: Modifier =
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
    val memoriesRef = FirebaseFirestore.getInstance().collection("Memories")
    val coroutineScope = rememberCoroutineScope()
    var auth = Firebase.auth
    var email = auth.currentUser?.email
    val userDocumentId = remember { mutableStateOf<String?>(null) }
    var competitionCode by remember { mutableStateOf("") }

    var selectedMatch by remember { mutableStateOf<MatchInfo?>(null) }
    var expandedMatch by remember { mutableStateOf(false) }
    //GPS
    main.locationPremissionRequest.launch(arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ))
    var lat by remember { mutableStateOf(0.0) }
    var log by remember { mutableStateOf(0.0) }
    LaunchedEffect(email) {
        userDocumentId.value = getDocumentIdByEmail(email)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Add Memory",
                modifier = Modifier
                    .padding(start = 16.dp),
                style = TextStyle(
                    color = Color(0xFF2462C2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )
        }
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
            Log.d("location", main.stat.latitude.toString() +"   " +main.stat.longitude.toString())
            lat = main.stat.latitude
            log = main.stat.longitude
            //location = main.stat.latitude.toString()+"   "+ main.stat.longitude.toString()
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
                    if (match.id.toInt() != -1){
                        Text(
                            text = "${match.homeTeam?.name} vs ${match.awayTeam?.name}",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }else{
                        Text(text = "(Current)FCUL Event CM",
                            modifier = Modifier.padding(top = 8.dp))
                    }

                }
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select a League", tint = Color(0xFF2462C2))
            }

            DropdownMenu(
                expanded = expandedMatch,
                onDismissRequest = { expandedMatch = false },
                modifier = Modifier.background(Color.White)
            ) {
                if ((lat < 39 && lat > 38 && log < -9 && log > -10)|| true){
                    DropdownMenuItem(
                        onClick = {
                            selectedMatch = MatchInfo(id = -1, null, null)
                            expandedMatch = false
                        }, text = { Text(text = "(Current)FCUL Event CM") }
                    )
                }

                // Iterate over matches and add them to the dropdown
                matches?.forEach { match ->
                    DropdownMenuItem(
                        onClick = {
                            selectedMatch = match
                            expandedMatch = false
                        }, text = { Text(text = "${match.homeTeam?.name} vs ${match.awayTeam?.name}") }
                    )
                }
            }
        }

        Button(onClick = {
            if (selectedMatch?.id?.toInt() == -1){
                val date = "2024-01-24"
                val title = "FCUL Event CM - 2024-01-24"
                val memory = hashMapOf(
                    "title" to title,
                    "dateOfCreation" to System.currentTimeMillis(),
                    "date" to date,
                    "images" to emptyList<String>(),
                    "matchId" to selectedMatch?.id,
                    "userId" to userDocumentId.value
                )
                memoriesRef.add(memory)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        coroutineScope.launch {
                            if (addMemoryToUser(userDocumentId.value.toString(), documentReference.id)) {
                                Toast.makeText(main.baseContext, "Memory added", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding memory document", e)
                    }
            }else if (selectedLeague == FootballLeague.OTHER) {
                val title = homeClub + " Vs " + awayClub + " - " + selectedDate.toString()
                val memory = hashMapOf(
                    "title" to title,
                    "dateOfCreation" to System.currentTimeMillis(),
                    "homeTeam" to homeClub,
                    "awayTeam" to awayClub,
                    "date" to selectedDate.toString(),
                    "userId" to userDocumentId.value


                )
                memoriesRef.add(memory)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }.addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }
            } else {
                val title = selectedMatch?.homeTeam?.name + " Vs " + selectedMatch?.awayTeam?.name + " - " + selectedDate.toString()
                val memory = hashMapOf(
                    "title" to title,
                    "dateOfCreation" to System.currentTimeMillis(),
                    "homeTeam" to selectedMatch?.homeTeam?.name,
                    "homeTeamCrest" to selectedMatch?.homeTeam?.crest,
                    "awayTeam" to selectedMatch?.awayTeam?.name,
                    "awayTeamCrest" to selectedMatch?.awayTeam?.crest,
                    "date" to selectedDate.toString(),
                    "images" to emptyList<String>(),
                    "matchId" to selectedMatch?.id,
                    "userId" to userDocumentId.value
                )
                memoriesRef.add(memory)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        coroutineScope.launch {
                            if (addMemoryToUser(userDocumentId.value.toString(), documentReference.id)) {
                                Toast.makeText(main.baseContext, "Memory added", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding memory document", e)
                    }
            }
            navController.navigate(Screens.MemoriesScreen.route)
        }) {
            Text(text = "Add Football Memory")
        }
    }
}


private suspend fun addMemoryToUser(currUserId : String, memoryDocumentId : String) : Boolean {
    val userRef = FirebaseFirestore.getInstance().collection("Users")

    return try {
        userRef
            .document(currUserId)
            .update("memories", FieldValue.arrayUnion(memoryDocumentId))
            .await()
        true
    } catch (e : java.lang.Exception) {
        false
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(Date(millis))
}







