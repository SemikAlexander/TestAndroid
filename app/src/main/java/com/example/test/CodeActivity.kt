package com.example.test

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.example.test.api.API
import com.example.test.databinding.ActivityCodeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityCodeBinding
    var code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onStart() {
        super.onStart()

        binding.apply {
            val editTexts = listOf(firstNumber, secondNumber, thirdNumber, fourthNumber)
            val telephone = intent.getStringExtra("telephone")

            editTexts.indices.forEach { i ->
                editTexts[i].doOnTextChanged { _, _, _, _ ->
                    if (emptyCheck(editTexts)) {
                        continueButton.visible(true)
                        resentTextView.visible(false)
                    }
                    else {
                        continueButton.visible(false)
                        resentTextView.visible(true)
                    }
                }
            }

            continueButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val answer = API.api.authenticateClients(telephone!!, code).execute()
                        launch(Dispatchers.Main) {
                            if (!answer.body()?.token.isNullOrEmpty()) {
                                val pref = getSharedPreferences("setting", Context.MODE_PRIVATE)
                                pref.edit().putString("userToken", answer.body()?.token)
                            }
                            else
                                toast("Пользователь не авторизирован в системе!")
                        }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) {
                            toast("Возникла проблема с получением токена!")
                        }
                    }
                }
            }
        }
    }

    private fun emptyCheck(editTexts: List<AppCompatEditText>): Boolean {
        binding.apply {
            for (i in editTexts.indices) {
                if (editTexts[i].text.isNullOrEmpty())
                    return false
                else
                    code += editTexts[i].text
            }
            return true
        }
    }
}