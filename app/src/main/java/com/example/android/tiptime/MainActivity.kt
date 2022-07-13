package com.example.android.tiptime

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.android.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchScreen = binding.switchScreens.setOnClickListener {
            val t = Intent(this, SecondActivity::class.java)
            startActivity(t)
        }


        binding.calculateButton.setOnClickListener {
            calculateTip()
        }

        binding.costOfServiceEditText.setOnKeyListener{view, keyCode, _ -> handleKeyEvent(view, keyCode)}
    }

    private fun calculateTip(){
        val stringCost = binding.costOfServiceEditText.text.toString()
        val cost = stringCost.toDoubleOrNull()
        if (cost == null || cost == 0.0) {
           displayTip(0.0)
            return
        }

        val tipPercent = when (binding.tipOptions.checkedRadioButtonId){
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        var tip = cost * tipPercent

        if (binding.roundUpSwitch.isChecked) tip = kotlin.math.ceil(tip)

        displayTip(tip)

    }

    private fun displayTip(tip:Double){
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

   private fun handleKeyEvent(view:View, keyCode: Int):Boolean{
       if (keyCode == KeyEvent.KEYCODE_ENTER){
           // Hide the keyboard
           val inputMethodManager =
               getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
           inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
           return true
       }
       return false
   }

}