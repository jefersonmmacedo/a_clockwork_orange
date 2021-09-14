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
    private var mListOfDaysScheduled = ArrayList<String>()
    private var mListSpMor = ArrayList<String>()
    private var mListSpAft = ArrayList<String>()
    private var mListSanMor = ArrayList<String>()
    private var mListSanAft = ArrayList<String>()
    private var listCount: ArrayList<DatesToExclude> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(USER)) {
            mUser = intent.getParcelableExtra(USER)!!
            println(mUser)
            mBinding.tvHello.text = "Olá, ${mUser.name} ${mUser.lastname}"
        }


        showProgressDialog()
        getScheduling()
        setupActionBar()

        mBinding.btnSchedule.setOnClickListener {
            val intent = Intent(this, SchedulingActivity::class.java)
            intent.putExtra(USERSCHEDULE, mUser)
            intent.putStringArrayListExtra(DATES_TO_EXCLUDE, mListOfDaysScheduled)
            startActivityForResult(intent, EDIT_CODE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCHEDULE_CODE || requestCode == EDIT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showProgressDialog()
                mUserScheduling = ArrayList<Schedulingdata.DateScheduling>()
                getScheduling()
                updateCharts(allSchedules)
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
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<ArrayList<Schedulingdata.DateScheduling>>,
                    response: Response<ArrayList<Schedulingdata.DateScheduling>>
                ) {
                    allSchedules = response.body()!!

                    if (allSchedules.size > 0) {
                        for (i in allSchedules.indices) {
                            if (allSchedules[i]._idUser == mUser._id) {
                                mUserScheduling.add(allSchedules[i])
                                mListOfDaysScheduled.add(allSchedules[i].date!!)
                            }
                        }
                        updateCharts(allSchedules)
                        /*mUserScheduling.sort(a,b){
                            for (i in mUserScheduling.indices) {
                                if (i>0){
                                     (mUserScheduling[i].date)
                                }
                            }
                            // Turn your strings into dates, and then subtract them
                            // to get a value that is either negative, positive, or zero.
                            return new Date(b.date) - new Date(a.date);
                        });*/
                        populateDatesinRecycler(mUserScheduling)
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
        val currentDay = LocalDateTime.now().plusDays(1)
        val formatterDay = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatterHour = DateTimeFormatter.ofPattern("HH")
        val today = currentDay.format(formatterDay)
        val hour = currentDay.format(formatterHour)
        var countSaoPaulo = 0
        var countSantos = 0
        var shift = "Manhã"
        if (hour.toInt() > 13) {
            shift = "Tarde"
        }
        for (i in dates.indices) {
            if ((dates[i].date == today && dates[i].shift == shift && dates[i].type == "Estação de trabalho") || (dates[i].date == today && dates[i].shift == "Dia Inteiro" && dates[i].type == "Estação de trabalho")) {
                if (dates[i].location == "São Paulo") {
                    countSaoPaulo++
                } else {
                    countSantos++
                }
            }
        }
        val santosAvaibility = Constants.SANTOS_MAX_QUANTITY - countSantos
        mBinding.tvSantosQuantity.text = (" $santosAvaibility / ${Constants.SANTOS_MAX_QUANTITY}")

        santosChartView = mBinding.santosChartParam
        santosChartView.layoutParams.width =
            convertNumberToDisplayInChart(santosAvaibility, santosWidht)

        val saopauloAvaibility = Constants.SAO_PAULO_MAX_QUANTITY - countSaoPaulo
        mBinding.tvSaoPauloQuantity.text =
            (" $saopauloAvaibility / ${Constants.SAO_PAULO_MAX_QUANTITY}")

        saoPauloChartView = mBinding.saoPauloChartParam
        saoPauloChartView.layoutParams.width =
            convertNumberToDisplayInChart(saopauloAvaibility, saoPauloWidht)

        hideProgressDialog()
        //countDates()
    }

    fun editSchedule(position: Int) {
        val intent = Intent(this, EditScheduleActivity::class.java)
        intent.putExtra(EDITSCHEDULE, mUserScheduling)
        intent.putExtra(POSITION, position)
        startActivityForResult(intent, SCHEDULE_CODE)
    }

    companion object {
        var EDITSCHEDULE = "EditSchedule"
        var POSITION = "position"
        var USERSCHEDULE = "user"
        var SCHEDULE_CODE = 3
        var EDIT_CODE = 6
        var DATES_TO_EXCLUDE = "datesToExclude"
    }

    /*private fun countDates() {
        for (i in allSchedules.indices) {
            if (listCount.isEmpty()) {
                if (allSchedules[i].location == "São Paulo") {
                    when (allSchedules[i].shift) {
                        "Manhã" -> {
                            listCount.add(DatesToExclude((allSchedules[i].date), 1, 0, 0, 0))
                        }
                        "Tarde" -> {
                            listCount.add(DatesToExclude((allSchedules[i].date), 0, 1, 0, 0))
                        }
                        else -> {
                            listCount.add(DatesToExclude((allSchedules[i].date), 1, 1, 0, 0))
                        }
                    }
                } else {
                    when (allSchedules[i].shift) {
                        "Manhã" -> {
                            listCount.add(DatesToExclude((allSchedules[i].date), 0, 0, 1, 0))
                        }
                        "Tarde" -> {
                            listCount.add(DatesToExclude((allSchedules[i].date), 0, 0, 0, 1))
                        }
                        else -> {
                            listCount.add(DatesToExclude((allSchedules[i].date), 0, 0, 40, 1))
                        }
                    }
                }
            } else {
                for (a in listCount.indices) {
                    if (listCount[a].date == allSchedules[i].date) {
                        if (allSchedules[i].location == "São Paulo") {
                            if (allSchedules[i].shift == "Manhã") {
                                listCount[a].spCountShiftMornig++
                                if (listCount[a].spCountShiftMornig >= Constants.SAO_PAULO_MAX_QUANTITY) {
                                    mListSpMor.add(listCount[a].date!!)
                                }
                            } else if (allSchedules[i].shift == "Tarde") {
                                listCount[a].spCountShiftAfternoon++
                                if (listCount[a].spCountShiftAfternoon >= Constants.SAO_PAULO_MAX_QUANTITY) {
                                    mListSpAft.add(listCount[a].date!!)
                                }
                            } else {
                                listCount[a].spCountShiftMornig++
                                listCount[a].spCountShiftAfternoon++
                                if (listCount[a].spCountShiftMornig >= Constants.SAO_PAULO_MAX_QUANTITY) {
                                    mListSpMor.add(listCount[a].date!!)
                                }
                                if (listCount[a].spCountShiftAfternoon >= Constants.SAO_PAULO_MAX_QUANTITY) {
                                    mListSpAft.add(listCount[a].date!!)
                                }
                            }
                        } else {
                            if (allSchedules[i].shift == "Manhã") {
                                listCount[a].sanCountShiftMorning++
                                if (listCount[a].sanCountShiftMorning >= Constants.SANTOS_MAX_QUANTITY) {
                                    mListSanMor.add(listCount[a].date!!)
                                }
                            } else if (allSchedules[i].shift == "Tarde") {
                                listCount[a].sanCountShiftAfternoon++
                                if (listCount[a].sanCountShiftAfternoon >= Constants.SANTOS_MAX_QUANTITY) {
                                    mListSanAft.add(listCount[a].date!!)
                                }
                            } else {
                                listCount[a].sanCountShiftMorning++
                                listCount[a].sanCountShiftAfternoon++
                                if (listCount[a].sanCountShiftMorning >= Constants.SANTOS_MAX_QUANTITY) {
                                    mListSanMor.add(listCount[a].date!!)
                                }
                                if (listCount[a].sanCountShiftAfternoon >= Constants.SANTOS_MAX_QUANTITY) {
                                    mListSanAft.add(listCount[a].date!!)
                                }
                            }
                        }
                    } else {
                        if (allSchedules[i].location == "São Paulo") {
                            if (allSchedules[i].shift == "Manhã") {
                                listCount.add(DatesToExclude((allSchedules[i].date), 1, 0, 0, 0))
                            } else if (allSchedules[i].shift == "Tarde") {
                                listCount.add(DatesToExclude((allSchedules[i].date), 0, 1, 0, 0))
                            } else {
                                listCount.add(DatesToExclude((allSchedules[i].date), 1, 1, 0, 0))
                            }
                        } else {
                            if (allSchedules[i].shift == "Manhã") {
                                listCount.add(DatesToExclude((allSchedules[i].date), 0, 0, 1, 0))
                            } else if (allSchedules[i].shift == "Tarde") {
                                listCount.add(DatesToExclude((allSchedules[i].date), 0, 0, 0, 1))
                            } else {
                                listCount.add(DatesToExclude((allSchedules[i].date), 0, 0, 40, 1))
                            }
                        }
                    }
                }
            }
        }
        hideProgressDialog()
        println("Datas cheias sp manha: $mListSpMor")
        println("Datas cheias sp tarde: $mListSpAft")
        println("Datas cheias santos manha: $mListSanMor")
        println("Datas cheias santos tarde: $mListSanAft")
        println("Datas agendadas para excluir: $mListOfDaysScheduled")
        println("datas da lista: $listCount")
    }*/
}
