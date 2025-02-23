package com.lust.app.data.entities

data class Comment(
    val id: String,
    val name: String,
    val comment: String
) {
    constructor() : this("", "", "")
}
