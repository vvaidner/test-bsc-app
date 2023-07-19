package com.vvaidner.domain.core

fun interface ErrorProcessor {
    fun process(e: Throwable): Throwable
}