package com.vvaidner.data.remote

import com.vvaidner.data.remote.schema.TodoScheme
import retrofit2.http.*

interface TodosApi {

    @GET("todos?userId=1")
    suspend fun getTodos(): List<TodoScheme>
}