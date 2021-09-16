package com.squad34.aclockworkorange.network

import com.github.kittinunf.fuel.httpDelete
import com.squad34.aclockworkorange.models.*
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ClockworkService {


    @GET("validator/{email}")
    fun getEmailValidation(
        @Path("email") email: String
    ): Call<UserFromValidator>

    @GET("scheduling")
    fun getScheduling(
    ): Call<ArrayList<Schedulingdata.DateScheduling>>


    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserFromValidator>

    @FormUrlEncoded
    @POST("scheduling")
    fun scheduleDate(
        @Field("location") location: String,
        @Field("shift") shift: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("day") day: String,
        @Field("_idUser") _idUser: String,
        @Field("name") name: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("role") role: String

    ): Call<Schedulingdata.DateScheduling>

    @FormUrlEncoded
    @PUT("scheduling/{id}")
    fun updateSchedule(
        @Path("id") _id: String,
        @Field("location") location: String,
        @Field("shift") shift: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("day") day: String,
        @Field("_idUser") _idUser: String,
        @Field("name") name: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("role") role: String
    ): Call<Schedulingdata.DateScheduling>


    @DELETE("scheduling/{id}")
    fun deleteSchedule(
        @Path("id") id: String
    ): Call<Schedulingdata.DateScheduling>

    @FormUrlEncoded
    @POST("filter")
    fun totalPerDay(
        @Field("location") location: String,
        @Field("type") type: String,
        @Field("shift") shift: String,
        @Field("date") date: String

    ): Call<DateTotalPerDay>

    @FormUrlEncoded
    @POST("filter/user")
    fun userSchedule(
        @Field("_idUser") _idUser: String

    ): Call<DateSched>

    @FormUrlEncoded
    @POST("user")
    fun createUser(
        @Field("name") name: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("role") role: String,
        @Field("password") password: String

    ): Call<UserFromValidator>

    @FormUrlEncoded
    @PUT("user/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Field("name") name: String,
        @Field("lastname") lastname: String,
        @Field("email") email: String,
        @Field("role") role: String,
        @Field("password") password: String

    ): Call<UserFromValidator>

    @GET("securitycode/{securityCode}")
    fun getSecurity(
        @Path("securityCode") securityCode: String
    ): Call<SecurityResponse>

}