package com.example.app_test

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var buttonToggle: ImageButton
    private var isImageOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        buttonToggle = findViewById(R.id.buttonToggle)

        // 초기 버튼 설정
        updateButtonState()

        buttonToggle.setOnClickListener {
            isImageOn = !isImageOn // 상태를 토글
            updateButtonState() // 버튼 상태 업데이트
        }
    }

    private fun updateButtonState() {
        if (isImageOn) {
            //앱 기능 ON시 코드
            imageView.setImageResource(R.drawable.main_icon_on)
            buttonToggle.setImageResource(R.drawable.main_on_button)
        } else {
            //앱 기능 OFF 시 코드
            imageView.setImageResource(R.drawable.main_icon_off)
            buttonToggle.setImageResource(R.drawable.main_off_button)
        }
    }
}