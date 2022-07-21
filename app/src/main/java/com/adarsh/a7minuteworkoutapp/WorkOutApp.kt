package com.adarsh.a7minuteworkoutapp

import android.app.Application

class WorkOutApp : Application() {

    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}