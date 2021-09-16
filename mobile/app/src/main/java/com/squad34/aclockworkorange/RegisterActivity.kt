package com.squad34.aclockworkorange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.squad34.aclockworkorange.activities.BaseActivity
import com.squad34.aclockworkorange.activities.LoginActivity
import com.squad34.aclockworkorange.databinding.ActivityRegisterBinding
import com.squad34.aclockworkorange.models.DateSelected
import com.squad34.aclockworkorange.models.DateTotalPerDay
import com.squad34.aclockworkorange.models.UserFinal
import com.squad34.aclockworkorange.network.ClockworkService
import com.squad34.aclockworkorange.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : BaseActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mUser: UserFinal
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupDropdown()

        if (intent.hasExtra(LoginActivity.EMAIL)) {
            email = intent.getStringExtra(LoginActivity.EMAIL)!!
            mBinding.vwCode.visibility = View.VISIBLE
            mBinding.vwRegister.visibility = View.GONE
            mBinding.vwProfile.visibility = View.GONE
        }

        mBinding.btnSend.setOnClickListener {
            if (mBinding.etCode.text.toString() == "1020304050") {
                mBinding.etCode.setText("")
                showToastSuccess("Código válido!")
                mBinding.vwRegister.visibility = View.VISIBLE
                mBinding.vwCode.visibility = View.GONE
            } else {
                showToastError("Código inválido!")
                mBinding.etCode.setText("")
            }
        }
        var role = ""

        mBinding.actvRole.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                role = p0?.getItemAtPosition(p2).toString()
            }

        mBinding.btnCreateAccount.setOnClickListener {
            when {
                TextUtils.isEmpty(mBinding.etName.text.toString()) -> {
                    showToastAlert("Você deve preencher seu nome!")
                }
                TextUtils.isEmpty(mBinding.etSurname.text.toString()) -> {
                    showToastAlert("Você deve preencher seu sobrenome!")
                }
                TextUtils.isEmpty(mBinding.etPassword.text.toString()) -> {
                    showToastAlert("Você deve preencher sua senha!")
                }
                TextUtils.isEmpty(mBinding.etConfirmPassword.text.toString()) -> {
                    showToastAlert("Você deve preencher a confirmação da senha!")
                }
                TextUtils.isEmpty(role) -> {
                    showToastAlert("Você deve selecionar uma função!")
                }
                else -> {
                    if (mBinding.etPassword.text.toString() == mBinding.etConfirmPassword.text.toString()) {
                        showProgressDialog()
                        createUser(
                            mBinding.etName.text.toString(),
                            mBinding.etSurname.text.toString(),
                            email,
                            role,
                            mBinding.etPassword.text.toString()
                        )
                    } else {
                        showToastError("A confirmação da senha não confere, favor digitar novamente os dois campos!")
                        mBinding.etPassword.setText("")
                        mBinding.etConfirmPassword.setText("")
                    }
                }
            }
        }
    }

    fun setupDropdown() {

        val textFieldRoles: AutoCompleteTextView = mBinding.actvRole
        val roles = arrayListOf(
            "Fullstack Developer Jr",
            "Fullstack Developer Pleno",
            "Fullstack Developer Senior",
            "Front-end Developer Jr",
            "Front-end Developer Pleno",
            "Front-end Developer Senior",
            "Back-end Developer Jr",
            "Back-end Developer Pleno",
            "Back-end Developer Senior",
            "Mobile Developer Jr",
            "Mobile Developer Pleno",
            "Mobile Developer Senior",
            "UI/UX Designer Jr",
            "UI/UX Designer Pleno",
            "UI/UX Designer Senior",
            "Scrum Master",
            "Product Owner",
            "Agile Coach",
            "Gerente de Projetos",
            "QA"
        )
        val adapterRole = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, roles)
        textFieldRoles.setAdapter(adapterRole)
    }

    private fun createUser(
        name: String,
        lastname: String,
        email: String,
        role: String,
        password: String
    ) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.createUser(
                name,
                lastname,
                email,
                role,
                password
            )
            listCall.enqueue(object : Callback<UserFinal> {
                override fun onResponse(
                    call: Call<UserFinal>,
                    response: Response<UserFinal>
                ) {
                    var mUser = response.body()!!
                    showToastSuccess("Cadastro efetuado! Entre com email e senha.")
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    hideProgressDialog()
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(
                    call: Call<UserFinal>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

}