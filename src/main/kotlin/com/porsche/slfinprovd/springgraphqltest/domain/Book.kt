package com.porsche.slfinprovd.springgraphqltest.domain

import com.porsche.slfinprovd.springgraphqltest.graphql.types.BookGraphQLType
import java.util.Arrays
import java.util.function.Predicate

data class Book(
    val id: String,
    val title: String,
    val pages: Int,
    val authorId: String,
) {
    companion object {
        private val books: List<Book?> =
            Arrays.asList(
                Book("book-1", "Effective Java", 416, "author-1"),
                Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
                Book("book-3", "Down Under", 436, "author-3"),
            )

        fun getById(id: String): Book? = books
            .stream()
            .filter(Predicate({ book: Book? -> book!!.id == id }))
            .findFirst()
            .orElse(null)
    }
}

fun Book.toGraphQL(author: Author): BookGraphQLType = BookGraphQLType(
    id = id,
    name = title,
    pages = pages,
    author = author.toGraphQL(),
)
