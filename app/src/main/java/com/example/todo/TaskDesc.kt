package com.example.todo

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TaskDesc(
    name: String,
    description: String,
    category: String,
    checked: String,
    innerPadding: PaddingValues
)
{
    Column (modifier = Modifier.fillMaxSize().padding(innerPadding)){
        Text("Task :" , fontWeight = FontWeight.Bold , fontSize = 32.sp , modifier = Modifier.padding(10.dp))
        Text(text = name , fontSize = 28.sp , modifier = Modifier.padding(horizontal = 10.dp))

        Text("Description :" , fontWeight = FontWeight.SemiBold , fontSize = 30.sp , modifier = Modifier.padding(10.dp))
        Text(text = description , fontSize = 24.sp , modifier = Modifier.padding(horizontal = 10.dp ))
    }

}

fun DeleteTask(
    index: Int,
    coroutineScope: CoroutineScope,
    tasks: List<List<String>>,
    context: Context,
    navController: NavHostController
)
{
    coroutineScope.launch {
        val updatedTasks = tasks.toMutableList().apply { removeAt(index) }
        DataStoreManager.saveTasks(context, updatedTasks)
        Toast.makeText(context, "Task Deleted Successfully" , Toast.LENGTH_SHORT).show()
        navController.navigate(Home.route){
            popUpTo(0){inclusive=true}
        }
    }
}

@Composable
fun editTask(
    name: String,
    description: String,
    category: String,
    checked: String,
    innerPadding: PaddingValues,
    index: Int,
    navController: NavHostController,
)
{
    var taskname by remember { mutableStateOf(name) }
    var taskdesc by remember { mutableStateOf(description) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val tasksFlow = remember { DataStoreManager.getTasks(context) }
    val tasks by tasksFlow.collectAsState(initial = emptyList())
    Column (modifier = Modifier.fillMaxSize().padding(innerPadding)){
        Text("Task :" , fontWeight = FontWeight.Bold , fontSize = 32.sp , modifier = Modifier.padding(10.dp))
        TextField(
            value = taskname,
            onValueChange = {taskname = it},
            label = { Text("Enter task") },
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 28.sp
            )
        )


        Text("Description :" , fontWeight = FontWeight.SemiBold , fontSize = 30.sp , modifier = Modifier.padding(10.dp))
        TextField(
            value = taskdesc,
            onValueChange = {taskdesc = it},
            label = { Text("Enter Description") },
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 24.sp
            )
        )
        Column (Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
        { Button(onClick = {
            coroutineScope.launch {
                val updatedTasks = tasks.toMutableList()
                val edited_task= listOf(taskname,taskdesc,tasks[index][2],tasks[index][3])
                updatedTasks[index]=edited_task
                DataStoreManager.saveTasks(context, updatedTasks)
                val encodedTitle = Uri.encode(taskname)
                val encodedDescription = Uri.encode(taskdesc)
                Toast.makeText(context, "Edited Successfully" , Toast.LENGTH_SHORT).show()
                navController.navigate(Description.route+ "/$encodedTitle/$encodedDescription/$category/${checked}/${index}"){
                    popUpTo(edit.route){inclusive=true}
                    launchSingleTop = true
                }

            }
        }
        ) {
            Text("Save")
        }
        }

    }
}