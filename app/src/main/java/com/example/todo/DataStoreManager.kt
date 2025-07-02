package com.example.todo

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "todo_store")
object DataStoreManager {
    //key is named task_list
    private val TASK_LIST_KEY = stringPreferencesKey("task_list")

    //write
    suspend fun saveTasks(context: Context, tasks: List<List<String>>) {
        val json = Json.encodeToString(tasks)
        context.dataStore.edit { preferences ->
            preferences[TASK_LIST_KEY] = json
        }
    }

    //read , map means changing preferences to json
    fun getTasks(context: Context): Flow<List<List<String>>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[TASK_LIST_KEY] ?: "[]"
            Json.decodeFromString(json)
        }
    }
}