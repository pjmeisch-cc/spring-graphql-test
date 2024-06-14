package com.porsche.slfinprovd.springgraphqltest.graphql.types

import java.time.LocalDate

data class AuthorGraphQLType(
    val id: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
) {
    @JvmName("getFullName")
    fun fullName(): String = "$firstName $lastName"
}
