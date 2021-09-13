package com.squad34.aclockworkorange.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.activities.LoginActivity.Companion.USER
import com.squad34.aclockworkorange.adapters.SchedulesMainAdapter
import com.squad34.aclockworkorange.databinding.ActivityMainBinding
import com.squad34.aclockworkorange.models.*
import com.squad34.aclockworkorange.network.ClockworkService
import com.squad34.aclockworkorange.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var santosChartView: View
    private lateinit var saoPauloChartView: View
    private val saoPauloWidht: Double = 1.2625
    private val santosWidht: Double = 7.575
    private lateinit var mUser: UserFromValidator
    private var allSchedules = ArrayList<Schedulingdata.DateScheduling>()
    private var mUserScheduling = ArrayList<Schedulingdata.DateScheduling>()
    private var mMaxSaoPaulo = Constants.SAO_PAULO_MAX_QUANTITY
    private var mMaxSantos = Constants.SANTOS_MAX_QUANTITY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        if (intent.hasExtra(USER)) {
            mUser = intent.getParcelableExtra(USER)!!
            println(mUser)
            mBinding.tvHello.text = "Olá, ${mUser.name} ${mUser.lastname}"
        }

        getScheduling()












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
            intent.putExtra(USERSCHEDULE, mUser)
            startActivityForResult(intent, SCHEDULE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCHEDULE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getScheduling()
            }
        }
    }

    private fun populateDatesinRecycler(dates: ArrayList<Schedulingdata.DateScheduling>) {

        if (dates.indices.isEmpty()) {
            mBinding.llRv.visibility = View.GONE
            mBinding.tvNoDatesScheduled.visibility = View.VISIBLE
        } else {
            mBinding.llRv.visibility = View.VISIBLE
            mBinding.tvNoDatesScheduled.visibility = View.GONE

            mBinding.rvDatesInMain.layoutManager = LinearLayoutManager(this)
            mBinding.rvDatesInMain.setHasFixedSize(true)
            val adapter = SchedulesMainAdapter(this, dates)
            mBinding.rvDatesInMain.adapter = adapter
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

    fun getScheduling() {

        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.getScheduling()
            listCall.enqueue(object : Callback<ArrayList<Schedulingdata.DateScheduling>> {
                override fun onResponse(
                    call: Call<ArrayList<Schedulingdata.DateScheduling>>,
                    response: Response<ArrayList<Schedulingdata.DateScheduling>>
                ) {
                    allSchedules = response.body()!!

                    if (allSchedules.size > 0) {
                        for (i in allSchedules.indices) {
                            if (allSchedules[i]._idUser == mUser._id) {
                                println("Id nos agendamensos: ${allSchedules[i]._idUser}")
                                println("Id usuario: ${mUser._id}")
                                mUserScheduling.add(allSchedules[i])


                            }

                        }
                        populateDatesinRecycler(mUserScheduling)
                        println(mUserScheduling)
                    }

                }

                override fun onFailure(
                    call: Call<ArrayList<Schedulingdata.DateScheduling>>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
                }


            })


        } else {
            Toast.makeText(
                this,
                "Não foi possível baixar os dados, tente mais tarde!",
                Toast.LENGTH_LONG
            )
                .show()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCharts(dates: ArrayList<Schedulingdata.DateScheduling>) {
        val currentDay = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val today = currentDay.format(formatter)
        for (i in dates.indices) {
            if (dates[i].date == today) {

            }
        }
    }

    fun editSchedule(position: Int) {
        val intent = Intent(this, EditScheduleActivity::class.java)
        intent.putExtra(EDITSCHEDULE, mUserScheduling)
        intent.putExtra(POSITION, position)
        startActivity(intent)
    }

    companion object {
        var EDITSCHEDULE = "EditSchedule"
        var POSITION = "position"
        var USERSCHEDULE = "user"
        var SCHEDULE_CODE = 3

    }
}
