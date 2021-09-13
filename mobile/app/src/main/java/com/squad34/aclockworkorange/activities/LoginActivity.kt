package com.squad34.aclockworkorange.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.squad34.aclockworkorange.databinding.ActivityLoginBinding
import com.squad34.aclockworkorange.models.Token
import com.squad34.aclockworkorange.models.User
import com.squad34.aclockworkorange.models.UserFromValidator
import com.squad34.aclockworkorange.network.ClockworkService
import com.squad34.aclockworkorange.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mUser: UserFromValidator
    private var email = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())

        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        mBinding.btnAccess.setOnClickListener {

            val inputEmail = mBinding.etEmailAdress.text.toString()
            if (inputEmail.contains("@fcamara.com.br")) {
                if (Constants.isNetworkAvailable(this)) {
                    val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val service: ClockworkService = retrofit.create(ClockworkService::class.java)
                    val listCall = service.getEmailValidation(
                        inputEmail
                    )
                    listCall.enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {

                            if (response.body() == null) {
                                Toast.makeText(this@LoginActivity, "Email não cadastrado no sistema!", Toast.LENGTH_LONG).show()
                            }else{

                                email = response.body().toString()
                                mBinding.vwLoginEmail.visibility = View.GONE
                                mBinding.vwLoginPassword.visibility = View.VISIBLE

                                println(email)

                            }

                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("Erro", t.message.toString())
                        }

                    })

                }
            }else{
                Toast.makeText(this, "Você deve digitar seu email corporativo!", Toast.LENGTH_LONG).show()
            }


        }



        mBinding.btnLogin.setOnClickListener {

           val inputPassword = mBinding.etPassword.text.toString()

            if (Constants.isNetworkAvailable(this)) {
                val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service: ClockworkService = retrofit.create(ClockworkService::class.java)
                val listCall= service.login(
                        email,
                        inputPassword
                    )

                listCall.enqueue(object : Callback<UserFromValidator> {
                    override fun onResponse(
                        call: Call<UserFromValidator>,
                        response: Response<UserFromValidator>
                    ) {
                        if (response.isSuccessful){

                            mUser = UserFromValidator(0,"613df39d0248015f4766f9e8", "2021-09-12T12:33:33.067+00:00", "marcos@fcamara.com.br", "Fonseca", "Marcos", "", "Scrum Master", "2021-09-13T03:31:18.797+00:00", "")
                            intent()
                            /*mUser = response.body()!!

                            println("Mensagem de retorno : ${response.body().toString()}")
                            if (mUser.error == "User not found.") {
                                Toast.makeText(this@LoginActivity, "Email não cadastrado!", Toast.LENGTH_LONG).show()
                            } else if (mUser.error == "Invalid password.") {
                                Toast.makeText(this@LoginActivity, "Senha incorreta!", Toast.LENGTH_LONG).show()
                            } else {
                                mUser = response.body()!!

                                println(mUser.name)
                                intent()
                            }*/



                        }


                    }

                    override fun onFailure(call: Call<UserFromValidator>, t: Throwable) {
                        Log.e("Erro", t.message.toString())
                    }

                })


            }
        }
    }
    private fun intent() {
        val intent = Intent(this, MainActivity::class.java)
        println(mUser)
        intent.putExtra(USER, mUser)
        startActivity(intent)
    }

    companion object {
        var USER = "User"
    }
}