package com.example.s.nav

sealed class Screens (val route:String){
    object Sign: Screens("sign")
    object Regist: Screens("regis")
    object Done: Screens("done")
    object Edit: Screens("edit")
    object PostRegistForm : Screens("postregistform")

    object MemoriesScreen : Screens("memoriesscreen")
}