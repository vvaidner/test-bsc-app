package com.vvaidner.domain.core

import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import com.vvaidner.domain.Result
import com.vvaidner.domain.asResult
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<in Param, out Output>(private val coroutineContext: CoroutineContext) {

    protected abstract fun execute(param: Param): Flow<Output>

    fun resultFlow(param: Param): Flow<Result<Output>> =
        execute(param)
            .asResult()
            .flowOn(coroutineContext)
}