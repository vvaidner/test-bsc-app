package com.vvaidner.domain.core.cache

import java.util.concurrent.TimeUnit

interface InvalidationStrategy<Key> {
    fun record(key: Key, timestamp: Long = System.currentTimeMillis())
    fun validate(key: Key): Boolean
}

fun <Key> InvalidationStrategy<Key>.recordAndValidate(
    key: Key,
    timestamp: Long
): Boolean {
    record(key, timestamp)
    return validate(key)
}

data class TimeBasedInvalidationStrategy<Key>(
    val keepAliveTime: Long,
    val timeUnit: TimeUnit
) : InvalidationStrategy<Key> {

    private var systemTime: () -> Long = System::currentTimeMillis

    //    @VisibleForTesting
    constructor(
        keepAliveTime: Long,
        timeUnit: TimeUnit,
        systemTimeProvider: () -> Long
    ) : this(keepAliveTime, timeUnit) {
        systemTime = systemTimeProvider
    }

    private val records = mutableMapOf<Key, Long>()

    override fun record(key: Key, timestamp: Long) {
        records[key] = timestamp
    }

    override fun validate(key: Key): Boolean {
        val diff = systemTime.invoke() - records.getOrDefault(key, 0L)
        val isValid = diff < timeUnit.toMillis(keepAliveTime)
        if (isValid.not()) {
            deleteRecord(key)
        }
        return isValid
    }

    private fun deleteRecord(key: Key) {
        records.remove(key)
    }
}