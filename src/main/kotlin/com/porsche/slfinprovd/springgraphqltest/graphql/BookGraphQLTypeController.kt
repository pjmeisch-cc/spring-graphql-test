package com.porsche.slfinprovd.springgraphqltest.graphql

import com.porsche.slfinprovd.springgraphqltest.graphql.types.BookGraphQLType
import org.springframework.graphql.data.method.annotation.SchemaMapping

@GraphQLController
@SchemaMapping(typeName = "Book")
class BookGraphQLTypeController {
    @SchemaMapping()
    fun translatedName(book: BookGraphQLType) = book.translatedName()
}
