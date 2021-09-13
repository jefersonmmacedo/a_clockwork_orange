package com.squad34.aclockworkorange.models

object Token {
data class TokenId(
    val auth: Boolean,
    val token: String
)
}