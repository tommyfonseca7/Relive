package com.example.s.nav

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.s.MainActivity
import com.example.s.nav.screen.AddMemorie
import com.example.s.nav.screen.Done
import com.example.s.nav.screen.Edit
import com.example.s.nav.screen.GalleryScreen
import com.example.s.nav.screen.MemoriesScreen
import com.example.s.nav.screen.PostRegistForm
import com.example.s.nav.screen.Regist
import com.example.s.nav.screen.SearchUsers
import com.example.s.nav.screen.SignIn
import com.example.s.nav.screen.UserProfile

@Composable
fun NavGraph (navController: NavHostController, main: MainActivity){
    var flag = false
    NavHost(
        navController = navController,
        startDestination = Screens.Sign.route)
    {

        composable(route = Screens.Sign.route){
            SignIn(navController = navController, main = main )
        }

        composable(route = Screens.Done.route){
            Done(navController = navController )
        }

        composable(route = Screens.Regist.route){
            Regist(navController = navController, main = main )
        }

        composable(route = Screens.Edit.route){
            Edit(navController = navController, main = main )
        }

        composable(route = Screens.PostRegistForm.route) {
            PostRegistForm(navController = navController, main = main)
        }

        composable(Screens.MemoriesScreen.route) {
            MemoriesScreen(navController = navController, main = main)
        }

        composable(Screens.SearchUsers.route) {
            SearchUsers(navController = navController, main = main)
        }

        composable(Screens.AddMemorie.route) {
            AddMemorie(navController = navController, main = main)
        }
        composable(Screens.UserProfile.route) {
            UserProfile(navController = navController, main = main)
        }
        composable(route = Screens.Gallery.route+ "?sportType={sportType}"+ "?gameName={gameName}"){navBackStack ->
            //extracting the argument
            var type: String = navBackStack.arguments?.getString("sportType")!!
            var name: String = navBackStack.arguments?.getString("gameName")!!
            Log.d("dsad","dsa")
            GalleryScreen(navController = navController, main = main, sportType = type, gameName = name)
        }
    }
}