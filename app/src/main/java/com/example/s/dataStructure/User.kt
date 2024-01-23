package com.example.s.dataStructure

import com.google.firebase.Timestamp


data class User (
    val email : String = "",
    var name : String = "",
    var username : String = "",
    var Sports : List<String> = emptyList(),
    var friends : List<String> = emptyList(),
    var registrationDate: Timestamp? = null,
    var profileImageUrl: String? = null,
    var memories : List<String> = emptyList()
)

