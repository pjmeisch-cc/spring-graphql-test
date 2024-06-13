package com.porsche.slfinprovd.springgraphqltest.graphql

import com.porsche.slfinprovd.springgraphqltest.common.createUniqueTraceId
import com.porsche.slfinprovd.springgraphqltest.common.logger
import com.porsche.slfinprovd.springgraphqltest.common.removeTraceId
import com.porsche.slfinprovd.springgraphqltest.common.storeTraceId
import com.porsche.slfinprovd.springgraphqltest.graphql.RequestHeaderInterceptor.Companion.TRACE_ID
import graphql.ExecutionResult
import graphql.GraphQLContext
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.SimplePerformantInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationCreateStateParameters
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldCompleteParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.execution.instrumentation.parameters.InstrumentationFieldParameters
import graphql.execution.instrumentation.parameters.InstrumentationValidationParameters
import graphql.language.Document
import graphql.validation.ValidationError

var GraphQLContext.traceId: String?
    get() {
        return get(TRACE_ID)
    }
    set(value) {
        put(TRACE_ID, value)
    }

class TraceIdInstrumentationState(
    var traceId: String? = null,
) : InstrumentationState

class TraceIdInstrumentation : SimplePerformantInstrumentation() {
    private fun storeTraceId(state: TraceIdInstrumentationState?): Boolean = storeTraceId(state?.traceId, "graphql-request")

    private fun cleanTraceId(storedByOurselves: Boolean) {
        if (storedByOurselves) {
            removeTraceId()
        }
    }

    /**
     * if [state] is a TraceIdInstrumentationState, the traceId is stroed in the MDC, and an InstrumentationContext is
     * returned that removes the trace id from MDC, but only if on storing there wasn't already an entry in MDC.
     */
    private inline fun <reified T : Any> instrumentationContextWithCleanup(state: InstrumentationState?): InstrumentationContext<T> {
        val storedByOurselves = storeTraceId(state as? TraceIdInstrumentationState)
        return SimpleInstrumentationContext.whenCompleted { _, _ -> cleanTraceId(storedByOurselves) }
    }

    override fun createState(parameters: InstrumentationCreateStateParameters?): InstrumentationState? {
        val traceId = parameters?.executionInput?.graphQLContext?.traceId ?: createUniqueTraceId("graphql-request")
        return TraceIdInstrumentationState(traceId)
    }

    override fun beginExecution(
        parameters: InstrumentationExecutionParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<ExecutionResult>? = instrumentationContextWithCleanup(state)

    override fun beginParse(
        parameters: InstrumentationExecutionParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<Document>? = instrumentationContextWithCleanup(state)

    override fun beginValidation(
        parameters: InstrumentationValidationParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<MutableList<ValidationError>>? = instrumentationContextWithCleanup(state)

    override fun beginExecuteOperation(
        parameters: InstrumentationExecuteOperationParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<ExecutionResult>? = instrumentationContextWithCleanup(state)

    override fun beginSubscribedFieldEvent(
        parameters: InstrumentationFieldParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<ExecutionResult>? = instrumentationContextWithCleanup(state)

    override fun beginFieldExecution(
        parameters: InstrumentationFieldParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<Any>? = instrumentationContextWithCleanup(state)

    override fun beginFieldFetch(
        parameters: InstrumentationFieldFetchParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<Any>? = instrumentationContextWithCleanup(state)

    override fun beginFieldCompletion(
        parameters: InstrumentationFieldCompleteParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<Any>? = instrumentationContextWithCleanup(state)

    override fun beginFieldListCompletion(
        parameters: InstrumentationFieldCompleteParameters?,
        state: InstrumentationState?,
    ): InstrumentationContext<Any>? = instrumentationContextWithCleanup(state)

    companion object {
        private val LOG = logger()
    }
}
