package com.squad34.aclockworkorange.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.activities.LoginActivity.Companion.USER
import com.squad34.aclockworkorange.databinding.ActivityLoginBinding
import com.squad34.aclockworkorange.databinding.ActivityMainBinding
import com.squad34.aclockworkorange.models.User
import com.squad34.aclockworkorange.models.UserFinal
import com.squad34.aclockworkorange.models.UserFromValidator
import com.squad34.aclockworkorange.utils.Constants

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var santosChartView: View
    private lateinit var saoPauloChartView: View
    private val saoPauloWidht:Double = 1.2625
    private val santosWidht:Double = 7.575
    private lateinit var mUser: UserFromValidator
    private lateinit var mUserScheduling: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        if (intent.hasExtra(USER)) {
            mUser = intent.getParcelableExtra(USER)!!
        }

        mBinding.tvHello.text = "Olá, ${mUser.name} ${mUser.lastname}"



        setupActionBar()

        val santosOccupation = 32
        val saoPauloOccupation = 120

        mBinding.tvSantosQuantity.text = (" $santosOccupation / ${Constants.SANTOS_MAX_QUANTITY}")

        santosChartView = mBinding.santosChartParam
        santosChartView.layoutParams.width =
            convertNumberToDisplayInChart(santosOccupation, santosWidht)

        mBinding.tvSaoPauloQuantity.text =
            (" $saoPauloOccupation / ${Constants.SAO_PAULO_MAX_QUANTITY}")

        saoPauloChartView = mBinding.saoPauloChartParam
        saoPauloChartView.layoutParams.width =
            convertNumberToDisplayInChart(saoPauloOccupation, saoPauloWidht)

        mBinding.btnSchedule.setOnClickListener {
            val intent = Intent(this, SchedulingActivity::class.java)

            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarMainActivity)

        supportActionBar?.title = ""


        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mBinding.toolbarMainActivity.setNavigationOnClickListener {
            doubleBackToExit()
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_corner_up_left)

    }

    private fun convertNumberToDisplayInChart(n: Int, n2: Double): Int {
        return (n * n2 * this.resources.displayMetrics.density).toInt()
    }
}