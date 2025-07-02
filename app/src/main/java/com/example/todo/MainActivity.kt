package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo.ui.theme.TodoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent ()
        {
            TodoTheme ()
            {
                val TASK_LIST_KEY = stringPreferencesKey("task_list")
                val navController =rememberNavController()
                NavHost(navController, startDestination = "home")
                {
                    composable(Home.route)
                    {
                        Scaffold(
                            topBar = { MyTopAppBar(navController) }
                        ) {innerPadding ->
                            HomeScreen(innerPadding,navController)
                        }
                    }
                    composable(add.route)
                    {
                        Scaffold(
                            topBar = { AddAppBar(navController) }
                        ) {innerPadding->
                            AddScreen(innerPadding , navController)
                        }
                    }
                    composable(Description.route + "/{name}/{description}/{category}/{checked}/{index}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("description") { type = NavType.StringType },
                            navArgument("category") { type = NavType.StringType } ,
                            navArgument("checked") { type = NavType.StringType },
                            navArgument("index") { type = NavType.IntType }
                        )
                                //backstackentry is used to access arguments
                    ) {backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val description = backStackEntry.arguments?.getString("description") ?: ""
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        val checked = backStackEntry.arguments?.getString("checked") ?: ""
                        val index = backStackEntry.arguments?.getInt("index") ?: -1

                        Scaffold (topBar = { DescAppBar(navController,index) }){ innerPadding ->
                            TaskDesc(name,description,category,checked , innerPadding)
                        }

                    }

                    composable(edit.route + "/{name}/{description}/{category}/{checked}/{index}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("description") { type = NavType.StringType },
                            navArgument("category") { type = NavType.StringType } ,
                            navArgument("checked") { type = NavType.StringType },
                            navArgument("index") { type = NavType.IntType }
                        )
                        //backstackentry is used to access arguments
                    ) {backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val description = backStackEntry.arguments?.getString("description") ?: ""
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        val checked = backStackEntry.arguments?.getString("checked") ?: ""
                        val index = backStackEntry.arguments?.getInt("index") ?: -1

                        Scaffold (topBar = { EditAppBar(navController,index) }){ innerPadding ->
                            editTask(name,description,category,checked , innerPadding ,index , navController )
                        }

                    }
                }
            }
        }
    }
}

