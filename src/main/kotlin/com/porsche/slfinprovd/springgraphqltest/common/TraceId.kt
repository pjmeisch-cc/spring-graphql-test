package com.porsche.slfinprovd.springgraphqltest.common

import org.slf4j.MDC
import java.util.Base64
import java.util.UUID

const val X_TRACE_ID = "x-trace-id"

/**
 * stores a traceid in the MDC if there is no one yet.
 * @see createUniqueTraceId about how the id is created
 * @return true if the trace id was newly stored, false if there already existed
 * one
 */
fun storeTraceId(
    traceId: String?,
    tracePrefix: String,
): Boolean =
    when {
        MDC.get(X_TRACE_ID) != null -> false
        else -> {
            MDC.put(X_TRACE_ID, traceId ?: createUniqueTraceId(tracePrefix))
            true
        }
    }

/**
 * removes the traceid from the MDC
 */
fun removeTraceId() {
    MDC.remove(X_TRACE_ID)
}

/**
 * if the MDC contains a value for [X_TRACE_ID] then the trace id value is returned, otherwise null.
 */
fun traceId(): String? = MDC.get(X_TRACE_ID)

fun createUniqueTraceId(prefix: String) = "$prefix-${Base64.getUrlEncoder().encodeToString(UUID.randomUUID().toString().toByteArray())}"
