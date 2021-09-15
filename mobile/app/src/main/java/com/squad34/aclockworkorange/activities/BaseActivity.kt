package com.squad34.aclockworkorange.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.getSystemService
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.databinding.ActivityBaseBinding.inflate
import kotlinx.android.synthetic.main.dialog_ok.*

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
        mProgressDialog.setContentView(R.layout.dialog_custom_progress)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun showToast(text: String) {
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_LONG
        toast.setText(text)
        val view = layoutInflater.inflate(R.layout.dialog_ok, null)
        toast.view = view
        toast.show()

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