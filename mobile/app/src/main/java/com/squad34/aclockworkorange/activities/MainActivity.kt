package com.squad34.aclockworkorange.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.databinding.ActivityLoginBinding
import com.squad34.aclockworkorange.databinding.ActivityMainBinding
import com.squad34.aclockworkorange.utils.Constants

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var santosChartView: View
    private lateinit var saoPauloChartView: View
    private val saoPauloWidht:Double = 1.2625
    private val santosWidht:Double = 7.575

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

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