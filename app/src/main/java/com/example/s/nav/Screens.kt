package com.example.s.nav

sealed class Screens (val route:String){
    object Sign: Screens("sign")
    object Regist: Screens("regis")
    object Done: Screens("done")
    object Edit: Screens("edit")
    object PostRegistForm : Screens("postregistform")
    object SearchUsers : Screens("searchusers")
    object MemoriesScreen : Screens("memoriesscreen")
    object AddMemorie : Screens("addmemorie")
    object Gallery : Screens("gallery/{gameName}")
    object UserProfile : Screens("userprofile")

    object Detail : Screens("detail")
}