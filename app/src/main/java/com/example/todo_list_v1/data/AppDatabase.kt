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

@Database(entities = [Task::class, Category::class, CompletedTask::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun completedTaskDao(): CompletedTaskDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tasks ADD COLUMN expectedStartTime INTEGER")
                database.execSQL("ALTER TABLE tasks ADD COLUMN expectedStopTime INTEGER")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tasks ADD COLUMN category TEXT")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tasks ADD COLUMN categoryId INTEGER")
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

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new recurrence-related columns to the tasks table
                database.execSQL("ALTER TABLE tasks ADD COLUMN repeatInterval INTEGER")
                database.execSQL("ALTER TABLE tasks ADD COLUMN repeatEndsAt INTEGER")
                database.execSQL("ALTER TABLE tasks ADD COLUMN repeatOnDays TEXT")  // Store as a comma-separated string
                database.execSQL("ALTER TABLE tasks ADD COLUMN nextOccurrence INTEGER")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the completed_tasks table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `completed_tasks` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `taskId` INTEGER,
                        `taskName` TEXT NOT NULL,
                        `taskDescription` TEXT,
                        `taskCategory` TEXT,
                        `taskDueDate` INTEGER,
                        `completedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`taskId`) REFERENCES `tasks`(`id`) ON DELETE SET NULL
                    )
                    """
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Rename the existing table to a temporary table
                database.execSQL("ALTER TABLE completed_tasks RENAME TO completed_tasks_temp")

                // Create a new table with the updated schema
                database.execSQL("""
            CREATE TABLE completed_tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                taskId INTEGER,
                taskName TEXT NOT NULL,
                taskDescription TEXT,
                taskCategory TEXT,
                taskDueDate INTEGER,
                completedAt INTEGER NOT NULL,
                FOREIGN KEY(taskId) REFERENCES tasks(id) ON DELETE SET NULL
            )
        """)

                // Copy data from the old table to the new table
                database.execSQL("""
            INSERT INTO completed_tasks (id, taskId, taskName, taskDescription, taskCategory, taskDueDate, completedAt)
            SELECT id, taskId, taskName, taskDescription, taskCategory, taskDueDate, completedAt
            FROM completed_tasks_temp
        """)

                // Drop the old table
                database.execSQL("DROP TABLE completed_tasks_temp")
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Rename the existing table to a temporary table
                database.execSQL("ALTER TABLE completed_tasks RENAME TO completed_tasks_temp")

                // Create a new table with the updated schema, including the new taskCategoryId column
                database.execSQL("""
                    
                    CREATE TABLE completed_tasks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        taskId INTEGER,
                        taskName TEXT NOT NULL,
                        taskDescription TEXT,
                        taskCategory TEXT,
                        taskCategoryId INTEGER, -- New column
                        taskDueDate INTEGER,
                        completedAt INTEGER NOT NULL,
                        FOREIGN KEY(taskId) REFERENCES tasks(id) ON DELETE SET NULL,
                        FOREIGN KEY(taskCategoryId) REFERENCES categories(id) ON DELETE SET NULL
                    )
                """)

                // Copy data from the old table to the new table
                database.execSQL("""
                    INSERT INTO completed_tasks (id, taskId, taskName, taskDescription, taskCategory, taskDueDate, completedAt)
                        SELECT id, taskId, taskName, taskDescription, taskCategory, taskDueDate, completedAt
                        FROM completed_tasks_temp
                    """)

                // Drop the old table
                database.execSQL("DROP TABLE completed_tasks_temp")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
                        MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}