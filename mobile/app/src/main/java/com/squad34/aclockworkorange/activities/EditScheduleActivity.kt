package com.squad34.aclockworkorange.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.models.Schedulingdata

class EditScheduleActivity : AppCompatActivity() {
    private var position = 0
    private lateinit var userSchedules: ArrayList<Schedulingdata.DateScheduling>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_schedule)

        if (intent.hasExtra(MainActivity.POSITION)) {
            position = intent.getIntExtra(MainActivity.POSITION,0)
        }
        if (intent.hasExtra(MainActivity.EDITSCHEDULE)) {
            userSchedules = intent.getParcelableArrayListExtra(MainActivity.EDITSCHEDULE)!!
        }

    }
}