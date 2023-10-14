package com.goldcompany.apps.data.data

data class Task(
    val id: Long = System.currentTimeMillis(),
    val isCompleted: Boolean,
    val title: String,
    val description: String,
    val startTimeMilli: Long?,
    val endTimeMilli: Long?
)