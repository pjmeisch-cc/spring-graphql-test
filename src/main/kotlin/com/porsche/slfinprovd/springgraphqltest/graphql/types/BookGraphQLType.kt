package com.porsche.slfinprovd.springgraphqltest.graphql.types

data class BookGraphQLType(
    val id: String,
    val name: String,
    val pages: Int,
    val author: AuthorGraphQLType,
) {
    fun translatedName() = TranslatedTextGraphQLType(text = name)
}
