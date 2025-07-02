package com.example.todo

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.todo.ui.theme.TODO_Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@Composable
fun HomeScreen(innerPadding: PaddingValues, navController: NavHostController) {

    val coroutineScope = rememberCoroutineScope()
    var filter_active = rememberSaveable { mutableIntStateOf(0) }
    Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        LazyRow {
            itemsIndexed(Filters.filters) { index,filter -> Display_Filter(index,filter , filter_active) }
            }


        val context = LocalContext.current
        val tasksFlow = remember { DataStoreManager.getTasks(context) }
        val tasks by tasksFlow.collectAsState(initial = emptyList())


        LazyColumn {
            itemsIndexed(tasks) { index,task ->
                when (filter_active.intValue)
                {
                    0 -> Display_Task(index,task,coroutineScope,tasks,context,navController)
                    1 -> if (task[3]=="0") Display_Task(index,task,coroutineScope,tasks,context,navController)
                    2 -> if (task[3]=="1") Display_Task(index,task,coroutineScope,tasks,context,navController)
                }

            }
        }

    }




}



@Composable
fun Display_Filter(index : Int, filter : String , active : MutableState<Int>)
{

    Button(onClick = {active.value=index}, shape = RoundedCornerShape(16.dp) , modifier = Modifier.padding(3.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (index==active.value) TODO_Color.LightGreen else Color.White)
    ) {
        Text(text=filter , color = Color.Black , fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Display_Task(
    index: Int,
    task: List<String>,
    coroutineScope: CoroutineScope,
    tasks: List<List<String>>,
    context: Context,
    navController: NavHostController
)
{
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = (task[3] == "1") ,
            onCheckedChange = { ischecked->
                coroutineScope.launch{
                    val updated = tasks.toMutableList()
                    val edited_task= listOf(task[0],task[1],task[2],if (ischecked) "1" else "0")
                    updated[index]=edited_task
                    DataStoreManager.saveTasks(context, updated)

                }
            }
        )

        Text(text = task[0] , modifier = Modifier.clickable {
            val encodedTitle = Uri.encode(task[0])
            val encodedDescription = Uri.encode(task[1])        //encoded so that special chars do not interfere
            val encodedCat = Uri.encode(task[2])

            navController.navigate(Description.route + "/$encodedTitle/$encodedDescription/$encodedCat/${task[3]}/${index}")
        })
    }

}
