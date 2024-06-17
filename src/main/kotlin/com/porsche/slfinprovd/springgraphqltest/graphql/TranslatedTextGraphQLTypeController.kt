package com.porsche.slfinprovd.springgraphqltest.graphql

import com.porsche.slfinprovd.springgraphqltest.graphql.types.TranslatedTextGraphQLType
import com.porsche.slfinprovd.springgraphqltest.graphql.types.TranslationGraphQLType
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping

@GraphQLController
@SchemaMapping(typeName = "TranslatedText")
class TranslatedTextGraphQLTypeController {
    @SchemaMapping
    fun translations(
        translatedText: TranslatedTextGraphQLType,
        @Argument languageTags: List<String>,
        env: DataFetchingEnvironment,
    ): List<TranslationGraphQLType> = translatedText.translations(languageTags, env)
}
