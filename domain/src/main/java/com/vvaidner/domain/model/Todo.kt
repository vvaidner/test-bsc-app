package com.vvaidner.domain.model

data class Todo(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean,
)
