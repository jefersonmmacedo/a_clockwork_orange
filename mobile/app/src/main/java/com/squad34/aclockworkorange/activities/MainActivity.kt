package com.squad34.aclockworkorange.activities

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
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
import kotlin.system.exitProcess

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
    private lateinit var mTotalSaoPaulo: DateTotalPerDay
    private lateinit var mTotalSantos: DateTotalPerDay

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openOptionsMenu()

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(USER)) {
            mUser = intent.getParcelableExtra(USER)!!
            println(mUser)
            mBinding.tvHello.text = "Olá, ${mUser.name} ${mUser.lastname}"

            getScheduling(mUser._id!!)
        }

        showProgressDialog()

        prepareDataToTotalPerDay()
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
                mUserDateSortedScheduling = ArrayList()
                getScheduling(mUser._id!!)

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
                .setPositiveButton("SIM"
                ) { dialogInterface, i ->
                    finishAffinity();
                    System.exit(0)
                }
                .setNegativeButton("NÃO"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }

            val alertDialog = alertDialogBuilder.create();

            alertDialog.show();
        }

        return super.onOptionsItemSelected(item)

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
                    println("Recebeu: $mUserScheduling")


                    var mUserSched = mUserScheduling.result
                    for (i in mUserSched.indices) {
                        mListOfDaysScheduled.add(mUserSched[i].date!!)
                    }
                    val dateTimeFormatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val datesSortedList: List<Schedulingdata.DateScheduling> =
                        mUserSched.sortedBy { LocalDate.parse(it.date, dateTimeFormatter) }
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
            Toast.makeText(
                this,
                "Não foi possível baixar os dados, tente mais tarde!",
                Toast.LENGTH_LONG
            )
                .show()
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
                    if (location == "São Paulo") {
                        mTotalSaoPaulo = response.body()!!
                        updateChartSaoPaulo(mTotalSaoPaulo.length)
                        println("Total SP: ${mTotalSaoPaulo.length}, a data: $date, o turno: $shift")

                    } else {
                        mTotalSantos = response.body()!!
                        updateChartSantos(mTotalSantos.length)
                        println("Total san: ${mTotalSantos.length}")
                    }
                }

                override fun onFailure(
                    call: Call<DateTotalPerDay>,
                    t: Throwable
                ) {
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

    companion object {
        var EDITSCHEDULE = "EditSchedule"
        var POSITION = "position"
        var USERSCHEDULE = "user"
        var SCHEDULE_CODE = 3
        var EDIT_CODE = 6
        var DATES_TO_EXCLUDE = "datesToExclude"
    }
}
