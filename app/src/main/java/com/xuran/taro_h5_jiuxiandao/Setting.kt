package com.xuran.taro_h5_jiuxiandao

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Setting : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.setting)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(
                R.id.setting
            )
        ) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 返回
        val buttonInstance = findViewById<Button>(R.id.button)
        buttonInstance.setOnClickListener {
            finish()
        }
        // 清除
        val btnClear = findViewById<Button>(R.id.button2)
        btnClear.setOnClickListener {
            AndroidInterface.getInstance().clearStore()
        }
    }
}