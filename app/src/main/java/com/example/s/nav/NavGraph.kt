package com.example.s.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.s.MainActivity
import com.example.s.dataStructure.Stat
import com.example.s.nav.screen.AddMemorie
import com.example.s.nav.screen.Done
import com.example.s.nav.screen.Edit
import com.example.s.nav.screen.GalleryScreen
import com.example.s.nav.screen.MemoriesScreen
import com.example.s.nav.screen.MemoryDetail
import com.example.s.nav.screen.PostRegistForm
import com.example.s.nav.screen.Regist
import com.example.s.nav.screen.SearchUsers
import com.example.s.nav.screen.SignIn
import com.example.s.nav.screen.UserProfile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun NavGraph (navController: NavHostController, main: MainActivity, p:Stat){
    NavHost(
        navController = navController,
        startDestination = Screens.Sign.route)
    {
        Log.d("123","dsa")
        composable(route = Screens.Sign.route){
            if (Firebase.auth.currentUser != null){
                main.change(1)
                MemoriesScreen(navController = navController, main = main, p=p)
            }else{
                main.change(0)
                SignIn(navController = navController, main = main )
            }

        }

        composable(route = Screens.Done.route){
            main.change(1)
            Done(navController = navController )
        }

        composable(route = Screens.Regist.route){
            main.change(0)
            Regist(navController = navController, main = main )
        }

        composable(route = Screens.Edit.route){
            main.change(0)
            Edit(navController = navController, main = main )
        }

        composable(route = Screens.PostRegistForm.route) {
            main.change(0)
            PostRegistForm(navController = navController, main = main)
        }

        composable(Screens.MemoriesScreen.route) {
            main.change(1)
            MemoriesScreen(navController = navController, main = main, p=p)
        }

        composable(Screens.SearchUsers.route) {
            main.change(1)
            SearchUsers(navController = navController, main = main)
        }

        composable(Screens.AddMemorie.route) {
            main.change(0)
            AddMemorie(navController = navController, main = main)
        }
        composable(Screens.UserProfile.route) {
            main.change(1)
            UserProfile(navController = navController, main = main, p= p)
        }
        composable(route = Screens.Gallery.route + "?gameName={gameName}"){navBackStack ->
            //extracting the argument
            main.change(1)
            var name: String = navBackStack.arguments?.getString("gameName")!!
            GalleryScreen(navController = navController, main = main, gameName = name)
        }
        composable(Screens.Detail.route) {
            main.change(1)
            MemoryDetail(navController = navController, p=p)
        }
    }
}