package com.example.s.dataStructure

import java.util.Date

data class Post (
    val id : Int? = 0,
    val userUID : String? = "",
    val date : Date? = null,
    val images : List<Int>? = null,
    val matchId: Long? = 0,
    val homeCrest : String? = null,
    val awayCrest : String? = null,
    val homeScore : Int? = 0,
    val awayScore : Int? = 0
    )