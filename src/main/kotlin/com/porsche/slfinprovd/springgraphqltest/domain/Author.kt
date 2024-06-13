package com.porsche.slfinprovd.springgraphqltest.domain

import com.porsche.slfinprovd.springgraphqltest.graphql.types.AuthorGraphQLType
import java.time.LocalDate
import java.util.Arrays
import java.util.function.Predicate

data class Author(val id: String, val firstName: String, val lastName: String, val birthDate: LocalDate) {
    companion object {
        private val authors: List<Author?> =
            Arrays.asList(
                Author("author-1", "Joshua", "Bloch", birthDate = LocalDate.of(1961, 8, 28)),
                Author("author-2", "Douglas", "Adams", birthDate = LocalDate.of(1952, 3, 11)),
                Author("author-3", "Bill", "Bryson", birthDate = LocalDate.of(1951, 12, 8)),
            )

        fun getById(id: String): Author? {
            return authors.stream().filter(Predicate({ author: Author? -> author!!.id == id })).findFirst().orElse(null)
        }
    }
}

fun Author.toGraphQL() =
    AuthorGraphQLType(
        id = id,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
    )
