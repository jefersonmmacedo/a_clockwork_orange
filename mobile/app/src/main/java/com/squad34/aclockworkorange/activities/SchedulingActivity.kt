package com.squad34.aclockworkorange.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.databinding.ActivitySchedulingBinding

class SchedulingActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySchedulingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        val textFieldUnits = mBinding.actvUnit as? AutoCompleteTextView
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits?.setAdapter(adapterUnit)

        val textFieldWorkStation = mBinding.actvWorkStation as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarScheduliongActivity)

        supportActionBar?.title = ""


        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mBinding.toolbarMainActivity.setNavigationOnClickListener {
            doubleBackToExit()
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_corner_up_left)

    }*/
}