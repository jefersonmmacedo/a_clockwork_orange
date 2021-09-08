package com.squad34.aclockworkorange.models

data class User(
    val user: String,
    val email: String,
    val role: String,
    val password: String,
    val scheduling: Date
)
