package com.example.todo

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.todo.ui.theme.TODO_Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavHostController)
{
    TopAppBar(title = { Text("TODO") },
//        navigationIcon = { IconButton(onClick = {/* TODO() */})
//            {Icon(imageVector = Icons.Default.Menu , contentDescription = "Menu")} },
        actions = {
            IconButton(onClick = {navController.navigate(add.route)}) { Icon(imageVector = Icons.Default.Add , contentDescription = "Add tasks") }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TODO_Color.DarkGreen
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppBar(navController: NavHostController)
{
    TopAppBar(title ={ Text("Add task") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TODO_Color.DarkGreen
        ),
        navigationIcon = { IconButton(onClick = {navController.navigate("home"){
            popUpTo(0){inclusive=true}
        } })
        {Icon(imageVector = Icons.Default.Close , contentDescription = "Menu")} }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescAppBar(navController: NavHostController, index : Int)
{
    val context = LocalContext.current
    val tasksFlow = remember { DataStoreManager.getTasks(context) }
    val tasks by tasksFlow.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(title ={ Text("Task Description") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TODO_Color.DarkGreen
        ),
        navigationIcon = { IconButton(onClick = {navController.navigate("home"){
            popUpTo(0){inclusive=true}
        } })
        {Icon(imageVector = Icons.Default.Close , contentDescription = "Menu")} },
        actions = {
            IconButton(onClick = {
                val encodedTitle = Uri.encode(tasks[index][0])
                val encodedDescription = Uri.encode(tasks[index][1])
                val encodedCat = Uri.encode(tasks[index][2])

                navController.navigate(edit.route + "/$encodedTitle/$encodedDescription/$encodedCat/${tasks[index][3]}/${index}")})
            { Icon(imageVector = Icons.Default.Edit , contentDescription = "Edit Task") }

            IconButton(onClick = {DeleteTask(index,coroutineScope,tasks,context, navController)}) { Icon(imageVector = Icons.Default.Delete , contentDescription = "Delete Task") }
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAppBar(navController: NavHostController, index: Int)
{
    TopAppBar(title ={ Text("Add task") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TODO_Color.DarkGreen
        ),
        navigationIcon = { IconButton(onClick = {navController.navigate("home"){
            popUpTo(0){inclusive=true}
        } })
        {Icon(imageVector = Icons.Default.Close , contentDescription = "Menu")} },



    )
}