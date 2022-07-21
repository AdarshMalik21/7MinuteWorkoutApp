package com.adarsh.a7minuteworkoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adarsh.a7minuteworkoutapp.databinding.ActivityExcerciseBinding
import com.adarsh.a7minuteworkoutapp.databinding.DialogCustomBackConformationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExcerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExcerciseBinding?= null

    private var restTimer: CountDownTimer?= null
    private var restProgress = 0
    private var restTimerDuration: Long = 1

    private var excerciseTimer: CountDownTimer?= null
    private var excerciseProgress = 0
    private var exerciseTimerDuration: Long = 1

    private var excerciseList:ArrayList<ExcerciseModel>? =null
    private var currentExercisePosition = -1

    private var tts:TextToSpeech? = null
    private var player:MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExcercise)

        if (supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        excerciseList = Constants.defaultExerciseList()


        tts = TextToSpeech(this, this)

        binding?.toolbarExcercise?.setNavigationOnClickListener {
            customDialogForBackButton()
//            onBackPressed()
        }
        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
//        super.onBackPressed()

    }

    private fun customDialogForBackButton(){

        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConformationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener {
            this@ExcerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {

            customDialog.dismiss()
        }
        customDialog.show()
    }


    private  fun  setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(excerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.adarsh.a7minuteworkoutapp/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExcerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpComingLable?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE


        if (restTimer!=null){
            restTimer?.cancel()
            restProgress = 0
        }



        binding?.tvUpcomingExerciseName?.text = excerciseList!![currentExercisePosition + 1].getname()
        setRestProgressBar()
    }
    private fun setupExcerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExcerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpComingLable?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if (excerciseTimer!=null){
            excerciseTimer?.cancel()
            excerciseProgress = 0
        }

        speakOut(excerciseList!![currentExercisePosition].getname())

        binding?.ivImage?.setImageResource(excerciseList!![currentExercisePosition].getimage())
        binding?.tvExerciseName?.text = excerciseList!![currentExercisePosition].getname()

        setExcerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.ProgressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration*1000,1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.ProgressBar?.progress = 10- restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                excerciseList!![currentExercisePosition].setisSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
               setupExcerciseView()
            }

        }.start()
    }

    private fun setExcerciseProgressBar(){
        binding?.ProgressBarExcercise?.progress = excerciseProgress

        excerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000,1000){
            override fun onTick(p0: Long) {
                excerciseProgress++
                binding?.ProgressBarExcercise?.progress = 30- excerciseProgress
                binding?.tvTimerExcercise?.text = (30 - excerciseProgress).toString()
            }

            override fun onFinish() {




                if (currentExercisePosition < excerciseList?.size!! -1){
                    excerciseList!![currentExercisePosition].setisSelected(false)
                    excerciseList!![currentExercisePosition].setisCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExcerciseActivity, Finish_Activity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer!= null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (excerciseTimer!=null){
            excerciseTimer?.cancel()
            excerciseProgress = 0
        }

        if (tts !=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player!=null){
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status ==  TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||  result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","Intialization Failed")
            }
        }
    }
    private fun speakOut(text:String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null ,"")
    }
}