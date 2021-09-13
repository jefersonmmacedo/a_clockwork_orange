package com.squad34.aclockworkorange.network

import com.squad34.aclockworkorange.models.Schedulingdata
import com.squad34.aclockworkorange.models.Token
import com.squad34.aclockworkorange.models.UserFromValidator
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
    ): Call<String>

    @GET("scheduling")
    fun getScheduling(
    ): Call<ArrayList<Schedulingdata.DateScheduling>>


    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserFromValidator>

}