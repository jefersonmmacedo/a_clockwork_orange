package com.squad34.aclockworkorange.models

object Schedulingdata class Scheduling(
    val __v: Int,
    val _id: Id,
    val createdAt: CreatedAt,
    val date: String,
    val email: String,
    val lastname: String,
    val location: String,
    val name: String,
    val role: String,
    val shift: String


)

data class Id(
    val `$oid`: String
)

data class CreatedAt(
    val `$date`: String
)