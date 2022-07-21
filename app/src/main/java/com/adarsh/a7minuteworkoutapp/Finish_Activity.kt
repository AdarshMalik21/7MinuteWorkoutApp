package com.adarsh.a7minuteworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.adarsh.a7minuteworkoutapp.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Finish_Activity : AppCompatActivity() {

    private var binding:ActivityFinishBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarfinishactivity)

        if (supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarfinishactivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val dao  = (application as WorkOutApp).db.historyDao()
        addDatetoDatabase(dao)
    }

    private fun addDatetoDatabase(historyDao: HistoryDao){

        try {
            val c = Calendar.getInstance()
            val dateTime = c.time
            Log.e("Date: ",""+dateTime)

            val sdf  = SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault())
            val date  = sdf.format(dateTime)
            Log.e("Formatted Date : ",""+ date)

            lifecycleScope.launch{
                historyDao.insert(HistoryEntity(date))
                Log.e(
                    "Date :",
                    "Added"
                )
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("FinishError","Error in finsih activity")
        }

    }
}