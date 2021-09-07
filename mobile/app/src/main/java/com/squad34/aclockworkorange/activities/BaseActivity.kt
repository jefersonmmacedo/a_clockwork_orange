package com.squad34.aclockworkorange.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.squad34.aclockworkorange.R

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)


    }

    fun showToast(context: Context = applicationContext, message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    fun showProgressDialog() {

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.custom_progress_dialog)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }


    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Por favor, clique no botão voltar novamente para sair.", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000
        )
    }


}