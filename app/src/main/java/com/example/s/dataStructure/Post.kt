package com.example.s.dataStructure

data class Post (
    val id : Int? = 0,
    val userId : String? = "",
    val date : String? = null,
    val images : List<Int>? = null,
    val matchId: Long? = 0,
    val homeTeamCrest : String? = null,
    val awayTeamCrest : String? = null,
    var homeScore : Int? = 0,
    var awayScore : Int? = 0
    )