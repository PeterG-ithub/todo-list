package com.example.todo_list_v1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryDao
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskDao
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TaskDao

@Database(entities = [Task::class, Category::class, CompletedTask::class], version = 9, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun completedTaskDao(): CompletedTaskDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new columns to the categories table
                database.execSQL("ALTER TABLE categories ADD COLUMN `order` INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE categories ADD COLUMN `isVisible` INTEGER NOT NULL DEFAULT 1")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .addMigrations(MIGRATION_8_9)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}