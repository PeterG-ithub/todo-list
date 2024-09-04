package com.example.todo_list_v1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Todolistv1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TodoApp()
                }
            }
        }
    }
}
