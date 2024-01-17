package com.example.s


data class User (
    val email : String = "",
    val name : String = "",
    val preferredSports : List<String> = emptyList()
)

/* Saving in the Database
fun saveUserInfoToDatabase(user: User) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    // Assuming the user ID is unique and can be used as a key in the database
    usersRef.child(user.userId).setValue(user)
}
* */