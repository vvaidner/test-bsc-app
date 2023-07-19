package com.vvaidner.data.remote.schema

import com.google.gson.annotations.SerializedName
import com.vvaidner.domain.model.Todo

data class TodoScheme(
    @SerializedName("userId")
    val userId: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("completed")
    val completed: Boolean,
)

internal fun TodoScheme.toDomain(): Todo {
   return Todo(
       userId = userId,
       id = id,
       title = title,
       completed = false
   )
}


internal fun List<TodoScheme>.toDomain(): List<Todo>{
   return map { it.toDomain() }
}