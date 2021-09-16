package com.squad34.aclockworkorange.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.databinding.ActivityRegisterBinding
import com.squad34.aclockworkorange.models.SecurityResponse
import com.squad34.aclockworkorange.models.UserFinal
import com.squad34.aclockworkorange.models.UserFromValidator
import com.squad34.aclockworkorange.network.ClockworkService
import com.squad34.aclockworkorange.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : BaseActivity() {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mUser: UserFromValidator

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
            mBinding.toolbarEditRegister.visibility = View.GONE
            mBinding.vwProfile.visibility = View.GONE
            mBinding.vwForgotPassword.visibility = View.GONE
        }

        if (intent.hasExtra(LoginActivity.FORGOT_PASSWORD)) {
            email = intent.getStringExtra(LoginActivity.FORGOT_PASSWORD)!!
            getUser(email)
            mBinding.vwCode.visibility = View.VISIBLE
            mBinding.vwRegister.visibility = View.GONE
            mBinding.toolbarEditRegister.visibility = View.GONE
            mBinding.vwProfile.visibility = View.GONE
            mBinding.vwForgotPassword.visibility = View.GONE
        }

        if (intent.hasExtra(MainActivity.USERSCHEDULE)) {
            mUser = intent.getParcelableExtra(MainActivity.USERSCHEDULE)!!
            mBinding.vwCode.visibility = View.GONE
            mBinding.vwRegister.visibility = View.GONE
            mBinding.vwProfile.visibility = View.VISIBLE
            mBinding.toolbarEditRegister.visibility = View.GONE
            mBinding.vwForgotPassword.visibility = View.GONE
            mBinding.tvNameProfile.text = "${mUser.name} ${mUser.lastname}"
            mBinding.tvRoleProfile.text = "Função: ${mUser.role}"
        }

        mBinding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        mBinding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        mBinding.btnHidePasswordReg.setOnClickListener {
            if (mBinding.etPassword.transformationMethod == PasswordTransformationMethod.getInstance()){
                mBinding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                mBinding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        mBinding.btnHidePasswordConReg.setOnClickListener {
            if (mBinding.etConfirmPassword.transformationMethod == PasswordTransformationMethod.getInstance()){
                mBinding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                mBinding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        mBinding.etPasswordForgot.transformationMethod = PasswordTransformationMethod.getInstance()
        mBinding.etConfirmPasswordForgot.transformationMethod = PasswordTransformationMethod.getInstance()

        mBinding.btnHidePasswordFor.setOnClickListener {
            if (mBinding.etPasswordForgot.transformationMethod == PasswordTransformationMethod.getInstance()){
                mBinding.etPasswordForgot.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                mBinding.etPasswordForgot.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        mBinding.btnHidePasswordForCon.setOnClickListener {
            if (mBinding.etConfirmPasswordForgot.transformationMethod == PasswordTransformationMethod.getInstance()){
                mBinding.etConfirmPasswordForgot.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                mBinding.etConfirmPasswordForgot.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }



        mBinding.ivReturn.setOnClickListener {
            onBackPressed()
            finish()
        }

        mBinding.ivEditReturn.setOnClickListener {
            onBackPressed()
            finish()
        }

        mBinding.btnPasswordForgot.setOnClickListener {

            when {
                TextUtils.isEmpty(mBinding.etPasswordForgot.text.toString()) -> {
                    showToastAlert("Você deve preencher sua senha!")
                }
                TextUtils.isEmpty(mBinding.etConfirmPasswordForgot.text.toString()) -> {
                    showToastAlert("Você deve preencher a confirmação da senha!")
                }
                else -> {
                    if (mBinding.etPasswordForgot.text.toString().trim() == mBinding.etConfirmPasswordForgot.text.toString().trim()) {
                        showProgressDialog()
                        updateUserProfile(
                            mUser._id!!,
                            mUser.name!!,
                            mUser.lastname!!,
                            mUser.email!!,
                            mUser.role!!,
                            mBinding.etPasswordForgot.text.toString()
                        )
                    } else {
                        showToastError("A confirmação da senha não confere, favor digitar novamente os dois campos!")
                        mBinding.etPasswordForgot.setText("")
                        mBinding.etConfirmPasswordForgot.setText("")
                    }
                }
            }
        }



        mBinding.btnProfileEdit.setOnClickListener{

            mBinding.vwCode.visibility = View.GONE
            mBinding.vwRegister.visibility = View.VISIBLE
            mBinding.vwProfile.visibility = View.GONE
            mBinding.toolbarEditRegister.visibility = View.VISIBLE

            mBinding.etName.setText(mUser.name)
            mBinding.etSurname.setText(mUser.lastname)
            mBinding.etPassword.setText("")
            mBinding.etConfirmPassword.setText("")
            mBinding.btnCreateAccount.text = "Fazer Alterações"

        }

        mBinding.ivLogout.setOnClickListener{
            var alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("A Workclock Orange")
            alertDialogBuilder
                .setMessage("Você realmente deseja sair?")
                .setCancelable(false)
                .setPositiveButton(
                    "SIM"
                ) { dialogInterface, i ->
                    finishAffinity();
                    System.exit(0)
                }
                .setNegativeButton(
                    "NÃO"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        mBinding.ivEditLogout.setOnClickListener{
            var alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("A Workclock Orange")
            alertDialogBuilder
                .setMessage("Você realmente deseja sair?")
                .setCancelable(false)
                .setPositiveButton(
                    "SIM"
                ) { dialogInterface, i ->
                    finishAffinity();
                    System.exit(0)
                }
                .setNegativeButton(
                    "NÃO"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        mBinding.btnSend.setOnClickListener{
            getSecutityCode(mBinding.etCode.text.toString())
        }

        var role = ""

        mBinding.actvRole.onItemClickListener =
            AdapterView.OnItemClickListener{ p0, p1, p2, p3 ->
            role = p0?.getItemAtPosition(p2).toString()
        }

        mBinding.btnCreateAccount.setOnClickListener{
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
                    if (mBinding.etPassword.text.toString().trim() == mBinding.etConfirmPassword.text.toString().trim()) {

                        if (intent.hasExtra(MainActivity.USERSCHEDULE)) {
                            showProgressDialog()
                            mUser._id?.let { it1 ->
                                updateUserProfile(
                                    it1,
                                    mBinding.etName.text.toString().trim(),
                                    mBinding.etSurname.text.toString().trim(),
                                    mUser.email!!,
                                    role,
                                    mBinding.etPassword.text.toString().trim()
                                )
                            }
                        } else {
                            showProgressDialog()
                            createUser(
                                mBinding.etName.text.toString().trim(),
                                mBinding.etSurname.text.toString().trim(),
                                email,
                                role,
                                mBinding.etPassword.text.toString().trim()
                            )
                        }

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
            listCall.enqueue(object : Callback<UserFromValidator> {
                override fun onResponse(
                    call: Call<UserFromValidator>,
                    response: Response<UserFromValidator>
                ) {
                    var mUser = response.body()!!
                    showToastSuccess("Cadastro efetuado! Entre com email e senha.")
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    hideProgressDialog()
                    startActivity(intent)
                    finish()
                }

                override fun onFailure(
                    call: Call<UserFromValidator>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

    private fun updateUserProfile(
        _id: String,
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
            val listCall = service.updateUser(
                _id,
                name,
                lastname,
                email,
                role,
                password
            )
            listCall.enqueue(object : Callback<UserFromValidator> {
                override fun onResponse(
                    call: Call<UserFromValidator>,
                    response: Response<UserFromValidator>
                ) {
                    if (intent.hasExtra(LoginActivity.FORGOT_PASSWORD)) {
                        hideProgressDialog()
                        showToastSuccess("Nova senha criada com sucesso!")
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToastSuccess("Cadastro alterado!")
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        hideProgressDialog()
                        finish()
                    }

                }

                override fun onFailure(
                    call: Call<UserFromValidator>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }


    private fun getSecutityCode(
        securityCode: String
    ) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.getSecurity(
                securityCode
            )
            listCall.enqueue(object : Callback<SecurityResponse> {
                override fun onResponse(
                    call: Call<SecurityResponse>,
                    response: Response<SecurityResponse>
                ) {
                    val security = response.body()
                    println("Retorno: $security")
                    if (security == null) {
                        showToastError("Código inválido!")
                        mBinding.etCode.setText("")
                    } else {
                        if (intent.hasExtra(LoginActivity.FORGOT_PASSWORD)) {
                            mBinding.etCode.setText("")
                            showToastSuccess("Código correto!")
                            mBinding.vwForgotPassword.visibility = View.VISIBLE
                            mBinding.vwCode.visibility = View.GONE
                        } else {
                            mBinding.etCode.setText("")
                            showToastSuccess("Código correto!")
                            mBinding.vwRegister.visibility = View.VISIBLE
                            mBinding.vwCode.visibility = View.GONE
                        }

                    }

                }

                override fun onFailure(
                    call: Call<SecurityResponse>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

    private fun getUser(
        email: String
    ) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.getEmailValidation(
                email
            )
            listCall.enqueue(object : Callback<UserFromValidator> {
                override fun onResponse(
                    call: Call<UserFromValidator>,
                    response: Response<UserFromValidator>
                ) {
                    mUser = response.body()!!
                }

                override fun onFailure(
                    call: Call<UserFromValidator>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

    companion object {
        val USER_REG = "user_reg"
    }

}
