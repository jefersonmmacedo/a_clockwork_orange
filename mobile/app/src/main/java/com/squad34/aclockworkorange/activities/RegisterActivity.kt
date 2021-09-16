package com.squad34.aclockworkorange.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
        }

        if (intent.hasExtra(MainActivity.USERSCHEDULE)) {
            mUser = intent.getParcelableExtra(MainActivity.USERSCHEDULE)!!
            mBinding.vwCode.visibility = View.GONE
            mBinding.vwRegister.visibility = View.GONE
            mBinding.vwProfile.visibility = View.VISIBLE
            mBinding.toolbarEditRegister.visibility = View.GONE

            mBinding.tvNameProfile.text = "${mUser.name} ${mUser.lastname}"
            mBinding.tvRoleProfile.text = "Função: ${mUser.role}"
        }

        mBinding.ivReturn.setOnClickListener {
            onBackPressed()
            finish()
        }

        mBinding.ivEditReturn.setOnClickListener {
            onBackPressed()
            finish()
        }

        mBinding.btnProfileEdit.setOnClickListener {

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

        mBinding.ivLogout.setOnClickListener {
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

        mBinding.ivEditLogout.setOnClickListener {
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

        mBinding.btnSend.setOnClickListener {
            getSecutityCode(mBinding.etCode.text.toString())
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

                        if (intent.hasExtra(MainActivity.USERSCHEDULE)) {
                            showProgressDialog()
                            mUser._id?.let { it1 ->
                                updateUserProfile(
                                    it1,
                                    mBinding.etName.text.toString(),
                                    mBinding.etSurname.text.toString(),
                                    mUser.email!!,
                                    role,
                                    mBinding.etPassword.text.toString()
                                )
                            }
                        } else {
                            showProgressDialog()
                            createUser(
                                mBinding.etName.text.toString(),
                                mBinding.etSurname.text.toString(),
                                email,
                                role,
                                mBinding.etPassword.text.toString()
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
                    val user = response.body()!!
                    showToastSuccess("Cadastro alterado!")
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    intent.putExtra(USER_REG, user)
                    hideProgressDialog()
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
                        mBinding.etCode.setText("")
                        showToastSuccess("Código correto!")
                        mBinding.vwRegister.visibility = View.VISIBLE
                        mBinding.vwCode.visibility = View.GONE
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

    companion object {
        val USER_REG = "user_reg"
    }

}
