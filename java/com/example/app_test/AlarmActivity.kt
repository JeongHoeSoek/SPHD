package com.example.app_test


import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity: AppCompatActivity() {

    // 각 시간, 알람 세기, 알람 모드 설정에 사용되는 토글 버튼들을 리스트로 초기화
    private lateinit var timeToggleButtons: List<ToggleButton>
    private lateinit var levelToggleButtons: List<ToggleButton>
    private lateinit var modeToggleButtons: List<ToggleButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm) // 레이아웃 설정

        // 각 시간 설정 토글 버튼 초기화
        val toggleButton10Sec = findViewById<ToggleButton>(R.id.toggleButton_10_sec)
        val toggleButton30Sec = findViewById<ToggleButton>(R.id.toggleButton_30_sec)
        val toggleButton1Min = findViewById<ToggleButton>(R.id.toggleButton_1_min)
        val toggleButton3Min = findViewById<ToggleButton>(R.id.toggleButton_3_min)
        timeToggleButtons = listOf(toggleButton10Sec, toggleButton30Sec, toggleButton1Min, toggleButton3Min)

        // 알람 세기 설정 토글 버튼 초기화
        val toggleButtonLow = findViewById<ToggleButton>(R.id.toggleButton_low)
        val toggleButtonMedium = findViewById<ToggleButton>(R.id.toggleButton_medium)
        val toggleButtonHigh = findViewById<ToggleButton>(R.id.toggleButton_high)
        levelToggleButtons = listOf(toggleButtonLow, toggleButtonMedium, toggleButtonHigh)

        // 알람 모드 설정 토글 버튼 초기화
        val toggleButtonVibration = findViewById<ToggleButton>(R.id.toggleButton_vibration)
        val toggleButtonSound = findViewById<ToggleButton>(R.id.toggleButton_sound)
        modeToggleButtons = listOf(toggleButtonVibration, toggleButtonSound)

        setupToggleButtons() // 토글 버튼 설정 메소드 호출
    }

    // 토글 버튼 설정을 초기화하는 메소드
    private fun setupToggleButtons() {
        // 시간 설정 토글 버튼에 대한 동작 설정
        timeToggleButtons.forEach { button ->
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) { // 버튼이 선택되었을 때
                    timeToggleButtons.filter { it != button }.forEach { it.isChecked = false } // 다른 버튼의 선택 해제
                    button.setTextColor(Color.WHITE) // 선택된 버튼의 텍스트 색상 변경
                    button.setBackgroundResource(R.drawable.alarm_chip2) // 선택된 버튼의 배경 변경
                    // 선택된 시간에 따라 메시지 표시
                    when (button) {
                        timeToggleButtons[0] -> showToast("10초 설정")
                        timeToggleButtons[1] -> showToast("30초 설정")
                        timeToggleButtons[2] -> showToast("1분 설정")
                        timeToggleButtons[3] -> showToast("3분 설정")
                    }
                } else { // 버튼이 선택 해제되었을 때
                    button.setTextColor(Color.BLACK) // 버튼의 텍스트 색상 원래대로 변경
                    button.setBackgroundResource(R.drawable.alarm_chip) // 버튼의 배경 원래대로 변경
                }
            }
        }

        // 알람 세기 설정 토글 버튼에 대한 동작 설정
        levelToggleButtons.forEach { button ->
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) { // 버튼이 선택되었을 때
                    levelToggleButtons.filter { it != button }.forEach { it.isChecked = false } // 다른 버튼의 선택 해제
                    button.setTextColor(Color.WHITE) // 선택된 버튼의 텍스트 색상 변경
                    button.setBackgroundResource(R.drawable.alarm_chip2) // 선택된 버튼의 배경 변경
                    // 선택된 알람 세기에 따라 메시지 표시
                    when (button) {
                        levelToggleButtons[0] -> showToast("낮은 소리 설정")
                        levelToggleButtons[1] -> showToast("보통 소리 설정")
                        levelToggleButtons[2] -> showToast("높은 소리 설정")
                    }
                } else { // 버튼이 선택 해제되었을 때
                    button.setTextColor(Color.BLACK) // 버튼의 텍스트 색상 원래대로 변경
                    button.setBackgroundResource(R.drawable.alarm_chip) // 버튼의 배경 원래대로 변경
                }
            }
        }

        // 알람 모드 설정 토글 버튼에 대한 동작 설정
        modeToggleButtons.forEach { button ->
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) { // 버튼이 선택되었을 때
                    modeToggleButtons.filter { it != button }.forEach { it.isChecked = false } // 다른 버튼의 선택 해제
                    button.setTextColor(Color.WHITE) // 선택된 버튼의 텍스트 색상 변경
                    button.setBackgroundResource(R.drawable.alarm_long_chip2) // 선택된 버튼의 배경 변경
                    // 선택된 알람 모드에 따라 메시지 표시
                    when (button) {
                        modeToggleButtons[0] -> showToast("진동 모드 설정")
                        modeToggleButtons[1] -> showToast("소리 모드 설정")
                    }
                } else { // 버튼이 선택 해제되었을 때
                    button.setTextColor(Color.BLACK) // 버튼의 텍스트 색상 원래대로 변경
                    button.setBackgroundResource(R.drawable.alarm_long_chip) // 버튼의 배경 원래대로 변경
                }
            }
        }
    }

    // 간단한 시험용 메시지 Toast로 표시하는 메소드
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}