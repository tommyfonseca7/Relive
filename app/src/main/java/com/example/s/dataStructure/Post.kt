package com.example.s.dataStructure

data class Post (
    val id : Int? = 0,
    val userId : String? = "",
    val date : String? = "",
    val images : List<String>? = null,
    val matchId: Long? = 0,
    val homeTeamCrest : String? = "",
    val awayTeamCrest : String? = "",
    var homeScore : Int? = 0,
    var awayScore : Int? = 0,
    var leagueName : String? = "",
    var venue : String? = "",
    var country : String? = "",
    var docId : String? = "",
    val homeTeam: String = "",
    val awayTeam: String = "",
    val title: String = "",

    )