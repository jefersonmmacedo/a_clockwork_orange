package com.squad34.aclockworkorange.models

data class DateTotalPerDay(
    val length: Int,
    val result: List<Result>
)
data class Result(
    val __v: Int,
    val _id: String,
    val _idUser: String,
    val createdAt: String,
    val date: String,
    val day: String,
    val email: String,
    val lastname: String,
    val location: String,
    val name: String,
    val role: String,
    val shift: String,
    val type: String
)