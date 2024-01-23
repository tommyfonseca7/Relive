package com.example.s

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.s.nav.NavGraph
import com.example.s.nav.Screens
import com.example.s.nav.screen.Camera
import com.example.s.ui.theme.STheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


class MainActivity : ComponentActivity() {

    lateinit var  navController :NavHostController

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            STheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()

                    BottomBar()
                }
            }
        }
    }

    fun changeToPhoto(){
        val intent = Intent(this, Camera::class.java)
        startActivity(intent)
    }
    @Composable
    fun BottomBar(){
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }

        val currentRoute = currentRoute(navController)

        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar(currentRoute)){
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    if (selectedItemIndex == 0){
                                        navController.navigate(Screens.MemoriesScreen.route)
                                    }else{
                                        navController.navigate(Screens.UserProfile.route)
                                    }
                                },
                                label = {
                                    Text(text = item.title)
                                },
                                alwaysShowLabel = false,
                                icon = { Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )

                                }
                            )
                        }
                    }
                }
            }){ contentPadding ->
            Log.d("dsa", contentPadding.toString())
            NavGraph(navController = navController, this)
        }
    }
}


@Composable
private fun shouldShowBottomBar(route: String?): Boolean {
    // Specify the routes where you want to hide the bottom bar
    val routesWithoutBottomBar = listOf(Screens.AddMemorie.route)
    return route !in routesWithoutBottomBar
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

