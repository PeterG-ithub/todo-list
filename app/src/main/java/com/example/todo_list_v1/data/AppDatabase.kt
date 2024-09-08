package com.example.todo_list_v1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryDao
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TaskDao

@Database(entities = [Task::class, Category::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new columns to the existing table
                database.execSQL("ALTER TABLE tasks ADD COLUMN expectedStartTime INTEGER")
                database.execSQL("ALTER TABLE tasks ADD COLUMN expectedStopTime INTEGER")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN category TEXT")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new categoryId column
                database.execSQL("ALTER TABLE tasks ADD COLUMN categoryId INTEGER")

                // Add the foreign key constraint manually if necessary
                // (SQLite does not natively support foreign key constraints directly in ALTER TABLE statements)
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `categories` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `description` TEXT,
                `color` TEXT,
                `icon` TEXT,
                `createdDate` INTEGER NOT NULL,
                `lastUpdated` INTEGER NOT NULL,
                `isArchived` INTEGER NOT NULL DEFAULT 0
            )
            """
                )

                // Add index for the new column
                database.execSQL(
                    """
            CREATE INDEX IF NOT EXISTS `index_tasks_categoryId` ON `tasks` (`categoryId`)
            """
                )

                // Optionally, you can manually enforce foreign key constraints with SQL commands here
                database.execSQL("PRAGMA foreign_keys=ON")
            }
        }


        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")

                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4) // Add migration strategies here
                    .build()
                    .also { Instance = it }
            }
        }
    }
}