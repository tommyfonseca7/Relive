package com.example.s

import com.google.firebase.Timestamp


data class User (
    val email : String = "",
    var name : String = "",
    var username : String = "",
    var Sports : List<String> = emptyList(),
    var friends : List<String> = emptyList(),
    var registrationDate: Timestamp? = null,
    var profileImageUrl: String? = null
)

/* Saving in the Database
fun saveUserInfoToDatabase(user: User) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    // Assuming the user ID is unique and can be used as a key in the database
    usersRef.child(user.userId).setValue(user)
}
* */