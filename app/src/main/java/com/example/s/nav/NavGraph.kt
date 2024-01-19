package com.example.s.nav

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.s.nav.screen.Done
import com.example.s.nav.screen.Edit
import com.example.s.nav.screen.MemoriesScreen
import com.example.s.nav.screen.PostRegistForm
import com.example.s.nav.screen.Regist
import com.example.s.nav.screen.SignIn

@Composable
fun NavGraph (navController: NavHostController, main: Activity){
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
    }
}