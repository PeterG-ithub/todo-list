package com.example.todo_list_v1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    @Dao
    interface TaskDao {
        @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
        fun getAllTasks(): Flow<List<Task>>

        @Query("SELECT * FROM tasks WHERE id = :id")
        fun getTask(id: Int): Flow<Task?>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(task: Task)

        @Update
        suspend fun update(task: Task)

        @Delete
        suspend fun delete(task: Task)
    }
}