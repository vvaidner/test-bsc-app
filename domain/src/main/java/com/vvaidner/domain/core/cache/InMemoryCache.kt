package com.vvaidner.domain.core.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class InMemoryCache<Key, Value> : Cache<Key, Value> {
    private val hashMap: HashMap<Key, Value> = HashMap()

    private val mutex = Mutex()

    override suspend fun getAsync(input: Key): Value? = mutex.withLock {
        val invalidation = invalidationStrategy()
        invalidation ?: return@withLock hashMap[input]

        if (invalidation.validate(input)) {
            hashMap[input]
        } else {
            hashMap.remove(input)
            null
        }
    }

    override suspend fun setAsync(input: Key, output: Value) = mutex.withLock {
        invalidationStrategy()?.record(input)
        hashMap[input] = output
    }

    open fun invalidationStrategy(): InvalidationStrategy<Key>? = null

    suspend fun removeFromCache(input: Key) = mutex.withLock {
        hashMap.remove(input)
    }

    suspend fun clearCache() = mutex.withLock {
        hashMap.clear()
    }
}