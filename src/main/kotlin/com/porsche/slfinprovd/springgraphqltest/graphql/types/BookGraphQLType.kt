package com.porsche.slfinprovd.springgraphqltest.graphql.types

data class BookGraphQLType(
    val id: String,
    val name: String,
    val pageCount: Int,
    val author: AuthorGraphQLType,
)
