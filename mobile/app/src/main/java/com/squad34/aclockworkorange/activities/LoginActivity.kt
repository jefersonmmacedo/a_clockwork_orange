package com.squad34.aclockworkorange.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding


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


            if (inputEmail.contains("")) {
                mBinding.vwLoginEmail.visibility = View.GONE
                mBinding.vwLoginPassword.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    this,
                    "Email não cadastrado. Você deve inserir o seu email da FCamara",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        mBinding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            finish()
        }


    }
}