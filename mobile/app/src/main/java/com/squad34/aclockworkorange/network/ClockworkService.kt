package com.squad34.aclockworkorange.network

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
    ): Call<UserFromValidator>


    @POST("user/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ) : Call<Token.TokenId>
}