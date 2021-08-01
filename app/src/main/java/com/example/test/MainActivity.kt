package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.api.API
import com.example.test.api.info.Status
import com.example.test.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var numTelephone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            continueButton.setOnClickListener {
                numTelephone = editTextPhone.text.toString().replace("-", "")
                numTelephone = numTelephone.replace(" ", "")

                codeRequests("7$numTelephone")
            }
        }
    }

    private fun codeRequests(num: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val answer = API.api.requestSMSCodeClient(num).execute()
                launch(Dispatchers.Main) {
                    dataLoaded(answer.body())
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    dataLoaded(null)
                }
            }
        }
    }

    private fun dataLoaded(status: Status?) {
        if (status != null) {
            when (status.status){
                "success" -> startActivity<CodeActivity> {
                    putExtra("telephone", numTelephone)
                }
                else -> toast("Возникли проблемы с отправкой кода!")
            }
        }
    }
}