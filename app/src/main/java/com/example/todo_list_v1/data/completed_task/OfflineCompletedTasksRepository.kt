package com.example.todo_list_v1.data.completed_task

import kotlinx.coroutines.flow.Flow

class OfflineCompletedTaskRepository(private val completedTaskDao: CompletedTaskDao) : CompletedTaskRepository {

    override fun getAllCompletedTasksStream(): Flow<List<CompletedTask>> =
        completedTaskDao.getAllCompletedTasks()

    override fun getCompletedTasksByTaskIdStream(taskId: Int): Flow<List<CompletedTask>> =
        completedTaskDao.getCompletedTasksByTaskId(taskId)

    override fun getCompletedTaskStream(id: Int): Flow<CompletedTask?> =
        completedTaskDao.getCompletedTaskById(id)

    override suspend fun insertCompletedTask(completedTask: CompletedTask) =
        completedTaskDao.insertCompletedTask(completedTask)

    override suspend fun updateCompletedTask(completedTask: CompletedTask) =
        completedTaskDao.updateCompletedTask(completedTask)

    override suspend fun deleteCompletedTask(completedTask: CompletedTask) =
        completedTaskDao.deleteCompletedTask(completedTask)

    override suspend fun deleteAllCompletedTasks() =
        completedTaskDao.deleteAllCompletedTasks()
}
