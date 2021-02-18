package com.example.lab2

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mcc_mcn()
    }

    fun mcc_mcn() {
        var mcc = 0
        var mnc = 0
        val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperator = tel.networkOperator

        if (networkOperator == "") {
            mcc_mnc.text = "INSERT YOUR SIM"
        } else {
            mcc = networkOperator.substring(0, 3).toInt()
            mnc = networkOperator.substring(3).toInt()
            mcc_mnc.text = "MCC: " + mcc + " MNC: " + mnc
            if (mcc != 250) {
                where_you_are.text = "Russia is missing you, mate!"
            } else {
                where_you_are.text = "You are at home, mate!"
            }
        }
    }
}