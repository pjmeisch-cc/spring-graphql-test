package com.porsche.slfinprovd.springgraphqltest.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis

/**
 * simplified logger creation. Creates a logger for the current class (or enclosing class of companion object)
 */
fun Any.logger(): Logger {
    val clazz = if (this::class.isCompanion) this::class.java.enclosingClass.kotlin else this::class
    return logger(clazz)
}

fun logger(clazz: KClass<*>) = LoggerFactory.getLogger(clazz.java)!!

fun logger(name: String) = LoggerFactory.getLogger(name)!!

inline fun Logger.error(msg: (() -> String)) {
    if (isErrorEnabled && isLogEnabled) error(msg())
}

inline fun Logger.error(
    t: Throwable,
    msg: (() -> String),
) {
    if (isErrorEnabled && isLogEnabled) error(msg(), t)
}

inline fun Logger.warn(msg: (() -> String)) {
    if (isWarnEnabled && isLogEnabled) warn(msg())
}

inline fun Logger.warn(
    t: Throwable,
    msg: (() -> String),
) {
    if (isWarnEnabled && isLogEnabled) warn(msg(), t)
}

inline fun Logger.info(msg: (() -> String)) {
    if (isInfoEnabled && isLogEnabled) info(msg())
}

inline fun Logger.info(
    t: Throwable,
    msg: (() -> String),
) {
    if (isInfoEnabled && isLogEnabled) info(msg(), t)
}

inline fun Logger.debug(msg: (() -> String)) {
    if (isDebugEnabled && isLogEnabled) debug(msg())
}

inline fun Logger.debug(
    t: Throwable,
    msg: (() -> String),
) {
    if (isDebugEnabled && isLogEnabled) debug(msg(), t)
}

inline fun Logger.trace(msg: (() -> String)) {
    if (isTraceEnabled && isLogEnabled) trace(msg())
}

inline fun Logger.trace(
    t: Throwable,
    msg: (() -> String),
) {
    if (isTraceEnabled && isLogEnabled) trace(msg(), t)
}

const val DISABLE_LOG_KEY = "disable-log"

@Suppress("UnusedReceiverParameter")
val Logger.isLogEnabled: Boolean
    get() = MDC.get(DISABLE_LOG_KEY) != "true"

inline fun <T> withLoggingContext(
    vararg mdcEntries: Pair<String, String>,
    body: () -> T,
): T {
    try {
        mdcEntries.forEach { MDC.put(it.first, it.second) }
        return body()
    } finally {
        mdcEntries.forEach { MDC.remove(it.first) }
    }
}

inline fun <T> Logger.warnOnSlowExecution(
    name: String,
    maxTimeMs: Int,
    func: () -> T,
) = reportSlowExecution(name, maxTimeMs, this::warn, func)

inline fun <T> Logger.reportSlowExecution(
    name: String,
    maxTimeMs: Int,
    report: (msg: () -> String) -> Unit = this::info,
    func: () -> T,
): T {
    var result: T
    val timeMillis = measureTimeMillis { result = func() }
    if (timeMillis > maxTimeMs) {
        report { """SLOW execution "$name": duration: $timeMillis ms""" }
    }
    return result
}

fun reportValidationIssue(message: String) {
    logger("ValidationReport").info(message)
}
