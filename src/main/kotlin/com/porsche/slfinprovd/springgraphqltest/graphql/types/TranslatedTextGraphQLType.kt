package com.porsche.slfinprovd.springgraphqltest.graphql.types

import graphql.schema.DataFetchingEnvironment

data class TranslatedTextGraphQLType(
    val text: String,
) {
    fun translations(
        languageTags: List<String>,
        env: DataFetchingEnvironment?,
    ): List<TranslationGraphQLType> = env?.let {
//        val languageTags: Collection<String> = env.getArgument("languageTags") ?: emptySet()
        languageTags.map { languageTag ->
            TranslationGraphQLType(
                languageTag = languageTag,
                translation = "$text in $languageTag",
            )
        }
    } ?: emptyList()
}

data class TranslationGraphQLType(
    val languageTag: String,
    val translation: String?,
)
