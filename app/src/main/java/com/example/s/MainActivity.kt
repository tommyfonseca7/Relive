package com.example.s

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.s.dataStructure.Stat
import com.example.s.location.LocationManager
import com.example.s.nav.NavGraph
import com.example.s.nav.Screens
import com.example.s.nav.screen.Camera
import com.example.s.nav.screen.MemoryDetail
import com.example.s.ui.theme.STheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


class MainActivity : ComponentActivity() {

    lateinit var  navController :NavHostController
    val stat : Stat by mutableStateOf(Stat())
    var screen : Int by mutableStateOf(0)

    val locationPremissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permisions ->
        when{
            permisions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false)->{
                LocationManager.Builder
                    .create(this@MainActivity)
                    .request(onUpdateLocation = { latitude: Double, longitude: Double ->
                        LocationManager.removeCallback(this@MainActivity)
                        stat.latitude = latitude
                        stat.longitude = longitude
                    })
            }
            permisions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false)->{
                LocationManager.Builder
                    .create(this@MainActivity)
                    .request(onUpdateLocation = { latitude: Double, longitude: Double ->
                        LocationManager.removeCallback(this@MainActivity)
                        stat.latitude = latitude
                        stat.longitude = longitude
                    })
            }

            else ->{
                //LocationManager.goSettingScrren(this@MainActivity)
            }

        }

    }

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavigationItem(
            title = "Add Memory",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add
        ),
        BottomNavigationItem(
            title = "Profile",
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

        Scaffold(
            bottomBar = {
                if (screen==1){
                    NavigationBar(modifier = Modifier.height(70.dp)) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                    if (selectedItemIndex == 0){
                                        navController.navigate(Screens.MemoriesScreen.route)
                                    }else if (selectedItemIndex == 1){
                                        navController.navigate(Screens.AddMemorie.route)
                                    } else {
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
                try {
                    Log.d("dsa", contentPadding.toString())

                }catch (e:Exception){

                }
            NavGraph(navController = navController, this, p=stat)
        }
    }
    fun change(i:Int){
        screen = i
    }
}


@Composable
private fun shouldShowBottomBar(route: String?): Boolean {
    // Specify the routes where you want to hide the bottom bar
    val routesWithoutBottomBar = listOf(Screens.AddMemorie.route, Screens.Sign.route, Screens.Regist.route, Screens.PostRegistForm.route)
    return route !in routesWithoutBottomBar
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

