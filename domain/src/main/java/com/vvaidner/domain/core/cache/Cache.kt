package com.vvaidner.domain.core.cache

interface Cache<Input, Output> {
    suspend fun getAsync(input: Input): Output?

    suspend fun setAsync(input: Input, output: Output)
}