package com.porsche.slfinprovd.springgraphqltest.graphql

import com.porsche.slfinprovd.springgraphqltest.HelloService
import com.porsche.slfinprovd.springgraphqltest.common.createUniqueTraceId
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RequestHeaderInterceptor(
    private val helloService: HelloService,
) : WebGraphQlInterceptor {
    public override fun intercept(
        request: WebGraphQlRequest,
        chain: WebGraphQlInterceptor.Chain,
    ): Mono<WebGraphQlResponse> {
        var traceId = request.headers.getFirst(TRACE_ID) ?: createUniqueTraceId("graphql-request")

        request.configureExecutionInput { _, builder ->
            builder
                .graphQLContext(
                    mapOf(
                        TRACE_ID to traceId,
                        "hello" to helloService,
                    ),
                ).build()
        }

        return chain.next(request)
    }

    companion object {
        const val TRACE_ID = "x-trace-id"
    }
}
