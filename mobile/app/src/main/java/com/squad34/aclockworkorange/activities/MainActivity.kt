package com.squad34.aclockworkorange.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.databinding.ActivityLoginBinding
import com.squad34.aclockworkorange.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarMainActivity)

        supportActionBar?.let{
            it.title = getString(R.string.main_activity_title)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarMainActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}