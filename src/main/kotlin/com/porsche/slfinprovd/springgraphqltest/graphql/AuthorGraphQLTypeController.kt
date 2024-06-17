package com.porsche.slfinprovd.springgraphqltest.graphql

import com.porsche.slfinprovd.springgraphqltest.graphql.types.AuthorGraphQLType
import org.springframework.graphql.data.method.annotation.SchemaMapping

@GraphQLController
@SchemaMapping(typeName = "Author")
class AuthorGraphQLTypeController {
    @SchemaMapping
    fun fullName(author: AuthorGraphQLType) = author.fullName()

    @SchemaMapping
    fun translatedName(author: AuthorGraphQLType) = author.translatedName()
}
