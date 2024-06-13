package com.porsche.slfinprovd.springgraphqltest.graphql

import graphql.GraphqlErrorBuilder
import graphql.scalars.ExtendedScalars
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLScalarType
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.DataFetcherExceptionResolver
import org.springframework.graphql.execution.ErrorType
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQLConfig {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer = RuntimeWiringConfigurer { builder ->
        builder.scalar(localDateScalar())
    }

    @Bean
    fun sourceBuilderCustomizer(): GraphQlSourceBuilderCustomizer = GraphQlSourceBuilderCustomizer { builder ->
        builder
            .instrumentation(listOf(TraceIdInstrumentation()))
            .exceptionResolvers(exceptionResolvers())
    }

    private fun exceptionResolvers(): List<DataFetcherExceptionResolver> {
        var exceptionResolver: DataFetcherExceptionResolver =
            DataFetcherExceptionResolver.forSingleError(
                { ex: Throwable, env: DataFetchingEnvironment? ->
                    GraphqlErrorBuilder
                        .newError(env)
                        .message(ex.message)
                        .errorType(ErrorType.BAD_REQUEST)
                        .build()
                },
            )
        return listOf(exceptionResolver)
    }

    fun localDateScalar(): GraphQLScalarType = ExtendedScalars.Date
}
