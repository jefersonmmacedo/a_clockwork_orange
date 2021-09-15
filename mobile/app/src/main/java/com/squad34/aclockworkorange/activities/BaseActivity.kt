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

    fun showProgressDialog() {

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_custom_progress)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun showToastSuccess(text: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.dialog_ok, findViewById(R.id.ct_success))
        val textView: TextView = layout.findViewById(R.id.tv_dialog_ok_text)
        textView.text = text
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    fun showToastError(text: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.dialog_error, findViewById(R.id.ct_error))
        val textView: TextView = layout.findViewById(R.id.tv_dialog_error_text)
        textView.text = text
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    fun showToastAlert(text: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.dialog_alert, findViewById(R.id.ct_alert))
        val textView: TextView = layout.findViewById(R.id.tv_dialog_alert_text)
        textView.text = text
        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
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