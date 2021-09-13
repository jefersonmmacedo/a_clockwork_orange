package com.squad34.aclockworkorange.models

import java.io.Serializable

object UserFinal {
}data class UserFromValidator(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val lastname: String,
    val name: String,
    val password: String,
    val role: String,
    val updatedAt: String
): Serializable