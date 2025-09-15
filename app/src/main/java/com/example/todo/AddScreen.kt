package com.example.todo

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddScreen(innerPadding: PaddingValues, navController: NavHostController)
{

    val context = LocalContext.current
    var taskname by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var completed by remember { mutableStateOf("0") }
    val coroutineScope = rememberCoroutineScope()
    val tasksFlow = remember { DataStoreManager.getTasks(context) }
    val tasks by tasksFlow.collectAsState(initial = emptyList())


    Column (modifier = Modifier.fillMaxWidth().padding(innerPadding) , verticalArrangement = Arrangement.Center)
    {
        Text(text= "TASK",modifier = Modifier.padding(top =30.dp , start = 5.dp), fontWeight = FontWeight.Bold , fontSize = 26.sp)
        TextField(
            value = taskname,
            onValueChange = {taskname = it},
            label = { Text("Enter task") },
            modifier = Modifier.padding(10.dp).fillMaxWidth()
        )
        Text(text= "Description" , modifier = Modifier
            .padding(top = 20.dp, start = 5.dp)
            .fillMaxWidth() , fontWeight = FontWeight.SemiBold , fontSize = 26.sp)
        TextField(
            value = description,
            onValueChange = {description = it},
            label = { Text("Enter task description") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        val task= listOf(taskname,description,category,completed)
        Column (Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
        { Button(onClick = {Save(task,coroutineScope,tasks,context , navController)} ) {
            Text("Save")
        }}
    }

}


fun Save(
    task: List<String>,
    coroutineScope: CoroutineScope,
    tasks: List<List<String>>,
    context: Context,
    navController: NavHostController
)
{
    if (task[0]!="")
    {
        coroutineScope.launch{
            val updated = tasks + listOf(task)
            DataStoreManager.saveTasks(context, updated)
        }
        Toast.makeText(context, "Task Added Successfully" , Toast.LENGTH_SHORT).show()
        navController.navigate(Home.route){
            popUpTo(0){inclusive=true}
        }
    }
    else
    {
        Toast.makeText(context, "Enter Task Name" , Toast.LENGTH_SHORT).show()
    }
}