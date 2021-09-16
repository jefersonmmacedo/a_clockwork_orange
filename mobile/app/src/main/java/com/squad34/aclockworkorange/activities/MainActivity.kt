package com.squad34.aclockworkorange.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.updateLayoutParams
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var santosChartView: View
    private lateinit var saoPauloChartView: View
    private val saoPauloWidht: Double = 1.2625
    private var santosWidht: Double = 7.575
    private lateinit var mUser: UserFromValidator
    private lateinit var mUserScheduling: DateSched
    private var mUserDateSortedScheduling = ArrayList<Schedulingdata.DateScheduling>()
    private var mListOfDaysScheduled = ArrayList<String>()
    private var mListOfScheduledMeetRoom = ArrayList<String>()
    private lateinit var mTotalSaoPaulo: DateTotalPerDay
    private lateinit var mTotalSantos: DateTotalPerDay

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openOptionsMenu()
        showProgressDialog()

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(USER)) {
            mUser = intent.getParcelableExtra(USER)!!
            println(mUser)
            mBinding.tvHello.text = "Olá, ${mUser.name} ${mUser.lastname}"

            getScheduling(mUser._id!!)
        }


        prepareDataToTotalPerDay()
        setupActionBar()

        mBinding.btnSchedule.setOnClickListener {
            val intent = Intent(this, SchedulingActivity::class.java)
            intent.putExtra(MEET_SCHEDULE, mListOfScheduledMeetRoom)
            intent.putExtra(USERSCHEDULE, mUser)
            intent.putStringArrayListExtra(DATES_TO_EXCLUDE, mListOfDaysScheduled)
            startActivityForResult(intent, EDIT_CODE)
        }

        mBinding.btnUser.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra(USERSCHEDULE, mUser)
            hideProgressDialog()
            startActivityForResult(intent, EDIT_USER)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCHEDULE_CODE || requestCode == EDIT_CODE || requestCode == EDIT_USER) {
            if (resultCode == Activity.RESULT_OK) {
                mUserDateSortedScheduling = ArrayList()
                getScheduling(mUser._id!!)
                if (intent.hasExtra(RegisterActivity.USER_REG)){
                    mUser = intent.getParcelableExtra(RegisterActivity.USER_REG)!!
                    mBinding.tvHello.text = "Olá, ${mUser.name} ${mUser.lastname}"
                }

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
        hideProgressDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            var alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("A Workclock Orange")
            alertDialogBuilder
                .setMessage("Você realmente deseja sair?")
                .setCancelable(false)
                .setPositiveButton(
                    "SIM"
                ) { dialogInterface, i ->
                    finishAffinity();
                    System.exit(0)
                }
                .setNegativeButton(
                    "NÃO"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarMainActivity)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_corner_up_left)
    }

    private fun getScheduling(id: String) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.userSchedule(
                id
            )
            listCall.enqueue(object : Callback<DateSched> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<DateSched>,
                    response: Response<DateSched>
                ) {
                    mUserScheduling = response.body()!!

                    var mUserSched = mUserScheduling.result
                    for (i in mUserSched.indices) {
                        if (mUserSched[i].type == "Estação de trabalho") {
                            mListOfDaysScheduled.add(mUserSched[i].date!!)
                        } else {
                            mListOfScheduledMeetRoom.add(mUserSched[i].date!!)
                        }
                    }

                    val dateTimeFormatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")

                    var mUserSched2: MutableList<Schedulingdata.DateScheduling> = mutableListOf()
                    for (i in mUserSched.indices) {
                        if (LocalDate.parse(mUserSched[i].date, dateTimeFormatter) >= LocalDate.now()) {
                            mUserSched2.add(mUserSched[i])
                        }
                    }

                    val datesSortedList: List<Schedulingdata.DateScheduling> =
                        mUserSched2.sortedBy { LocalDate.parse(it.date, dateTimeFormatter) }
                    for (i in datesSortedList.indices) {
                        mUserDateSortedScheduling.add(datesSortedList[i])
                    }
                    populateDatesinRecycler(mUserDateSortedScheduling)
                }

                override fun onFailure(
                    call: Call<DateSched>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

    private fun convertNumberToDisplayInChart(n: Int, n2: Double): Int {
        return (n * n2 * this.resources.displayMetrics.density).toInt()
    }

    fun updateChartSantos(totalSantos: Int) {

        val santosAvaibility = Constants.SANTOS_MAX_QUANTITY - totalSantos
        mBinding.tvSantosQuantity.text = (" $santosAvaibility / ${Constants.SANTOS_MAX_QUANTITY}")

        santosChartView = mBinding.santosChartParam


        santosChartView.updateLayoutParams {
            width = convertNumberToDisplayInChart(santosAvaibility, santosWidht)
        }
    }

    fun updateChartSaoPaulo(totalSaoPaulo: Int) {

        val saopauloAvaibility = Constants.SAO_PAULO_MAX_QUANTITY - totalSaoPaulo
        mBinding.tvSaoPauloQuantity.text =
            (" $saopauloAvaibility / ${Constants.SAO_PAULO_MAX_QUANTITY}")

        saoPauloChartView = mBinding.saoPauloChartParam
        saoPauloChartView.updateLayoutParams {
            width = convertNumberToDisplayInChart(saopauloAvaibility, saoPauloWidht)
        }
    }

    fun editSchedule(position: Int) {
        val intent = Intent(this, EditScheduleActivity::class.java)
        intent.putExtra(EDITSCHEDULE, mUserDateSortedScheduling)
        intent.putExtra(POSITION, position)
        intent.putExtra(DATES_TO_EXCLUDE, mListOfDaysScheduled)
        startActivityForResult(intent, SCHEDULE_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareDataToTotalPerDay() {
        var currentDay = LocalDateTime.now()
        val formatterDay = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatterHour = DateTimeFormatter.ofPattern("HH")
        var today = currentDay.format(formatterDay)
        val hour = currentDay.format(formatterHour)
        var shift = ""
        if (hour.toInt() in 13..18) {
            shift = "Tarde"
        } else if (hour.toInt() < 13) {
            shift = "Manhã"
        } else {
            shift = "Manhã"
            currentDay = LocalDateTime.now().plusDays(1)
            today = currentDay.format(formatterDay)
        }
        //Pega os dados para o gráfico de São Paulo para o turno atual
        getTotalPerDay("São Paulo", "Estação de trabalho", shift, today)

        //Pega os dados para o gráfico de Santos para o turno atual
        getTotalPerDay("Santos", "Estação de trabalho", shift, today)
    }


    private fun getTotalPerDay(location: String, type: String, shift: String, date: String) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.totalPerDay(
                location,
                type,
                shift,
                date
            )
            listCall.enqueue(object : Callback<DateTotalPerDay> {
                override fun onResponse(
                    call: Call<DateTotalPerDay>,
                    response: Response<DateTotalPerDay>
                ) {
                    var mTotal = response.body()!!
                    if (location == "São Paulo") {
                        mTotalSaoPaulo = response.body()!!
                        updateChartSaoPaulo(mTotalSaoPaulo.length)

                    } else {
                        mTotalSantos = response.body()!!
                        updateChartSantos(mTotalSantos.length)
                    }
                }

                override fun onFailure(
                    call: Call<DateTotalPerDay>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

    companion object {
        var EDITSCHEDULE = "EditSchedule"
        var POSITION = "position"
        var USERSCHEDULE = "user"
        var SCHEDULE_CODE = 3
        var EDIT_CODE = 6
        var DATES_TO_EXCLUDE = "datesToExclude"
        var MEET_SCHEDULE = "meetSchedule"
        var EDIT_USER = 9
    }
}
