package com.example.pausetimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var index :Int = 1
    private lateinit var secText: TextView
    private lateinit var milliText: TextView
    private lateinit var startBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var recordBtn: Button
    private lateinit var lap_Layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //View inflate
        secText = findViewById(R.id.secText)
        milliText = findViewById(R.id.milliText)
        startBtn = findViewById(R.id.startBtn)
        resetBtn = findViewById(R.id.resetBtn)
        recordBtn = findViewById(R.id.recordBtn)
        lap_Layout = findViewById(R.id.lap_Layout)

        //버튼 클릭 리스너
        startBtn.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) start() else pause()
        }
        resetBtn.setOnClickListener {
            reset()
        }
        recordBtn.setOnClickListener {
            if(time!=0) lapTime()
        }
    }
    // 시작
    private fun start() {
        startBtn.text ="중지"
        timerTask = kotlin.concurrent.timer(period = 10) { //반복주기는 peroid 프로퍼티로 설정, 단위는 1000분의 1초 (period = 1000, 1초)
            time++ // period=10으로 0.01초마다 time를 1씩 증가하게 됩니다
            val sec = time / 100 // time/100, 나눗셈의 몫 (초 부분)
            val milli = time % 100 // time%100, 나눗셈의 나머지 (밀리초 부분)

            // UI 조작을 위한 메서드
            runOnUiThread {
                secText.text = "$sec"
                milliText.text = "$milli"
            }
        }
    }
    // 일시정지
    private fun pause() {
        startBtn.text ="재실행"
        timerTask?.cancel();
    }
    // 초기화
    private fun reset() {
        timerTask?.cancel() // timerTask 가 null 이 아니라면 cancel() 호출

        time = 0 // 시간저장 변수 초기화
        isRunning = false // 현재 진행중인지 판별하기 위한 Boolean 변수 false 세팅
        secText.text = "0" // 시간(초) 초기화
        milliText.text = "00" // 시간(밀리초) 초기화

        startBtn.text ="시작"
        lap_Layout.removeAllViews() // Layout 에 추가한 기록View 모두 삭제
        index = 1
    }
    // 기록하기
    private fun lapTime() {
        val lapTime = time // 함수 호출 시 시간(time) 저장

        // apply() 스코프 함수로, TextView 를 생성과 동시에 초기화
        val textView = TextView(this).apply {
            textSize = 20f // fontSize 20 설정
        }
        textView.text = "${lapTime / 100}.${lapTime % 100}" // 출력할 시간 설정

        lap_Layout.addView(textView,0) // layout에 추가, (View, index) 추가할 위치(0 최상단 의미)
        index++ // 추가된 View 의 개수를 저장하는 index 변수
    }
}