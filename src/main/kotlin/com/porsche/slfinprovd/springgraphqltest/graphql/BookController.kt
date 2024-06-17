package com.porsche.slfinprovd.springgraphqltest.graphql

import com.porsche.slfinprovd.springgraphqltest.HelloService
import com.porsche.slfinprovd.springgraphqltest.common.info
import com.porsche.slfinprovd.springgraphqltest.common.logger
import com.porsche.slfinprovd.springgraphqltest.domain.Author
import com.porsche.slfinprovd.springgraphqltest.domain.Book
import com.porsche.slfinprovd.springgraphqltest.domain.toGraphQL
import com.porsche.slfinprovd.springgraphqltest.graphql.RequestHeaderInterceptor.Companion.TRACE_ID
import com.porsche.slfinprovd.springgraphqltest.graphql.types.BookGraphQLType
import graphql.GraphQLContext
import jakarta.validation.constraints.Size
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.QueryMapping

@GraphQLController
class BookController {
    @QueryMapping
    fun bookById(
        @Argument @Size(min = 3, message = "id must have a min length of 3") id: String,
        @ContextValue(TRACE_ID) traceId: String?,
        graphQLContext: GraphQLContext,
    ): BookGraphQLType? {
        LOG.info { "bookById($id); traceId: $traceId" }
        LOG.info { graphQLContext.get<HelloService>("hello").hello() }

        return Book.getById(id)?.let { book ->
            val bookGraphQLType =
                Author.getById(book.authorId)?.let { author ->
                    book.toGraphQL(author)
                }
            bookGraphQLType
        }
    }

    companion object {
        private val LOG = logger()
    }
}
