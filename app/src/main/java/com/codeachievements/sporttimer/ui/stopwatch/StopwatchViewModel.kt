package com.codeachievements.sporttimer.ui.stopwatch

import androidx.lifecycle.*
import com.codeachievements.sporttimer.R
import java.util.Timer
import java.util.TimerTask

class MillisecondTimer {
    private var _startTime = 0uL
    private var _stopTime = 0uL
    private var _timer: Timer? = null
    private val _time = MutableLiveData<ULong>().apply {
        value = 0uL
    }
    private val _isActive = MutableLiveData<Boolean>().apply {
        value = false
    }

    val time: LiveData<ULong> = _time
    val isActive: LiveData<Boolean> = _isActive
    fun start(){
        _startTime = System.currentTimeMillis().toULong()
        _isActive.value = true
        _timer = Timer(true)
        _timer?.schedule(object : TimerTask() {
                override fun run() {
                    val currentTime = System.currentTimeMillis().toULong()
                    _time.postValue(_stopTime + currentTime - _startTime)
                }
            }, 0L, 1L)
    }

    fun stop(){
        _timer?.cancel()
        _timer = null
        _stopTime = _time.value?:0uL
        _isActive.value = false
    }

    fun reset(){
        _startTime = System.currentTimeMillis().toULong()
        _stopTime = 0uL
        _time.postValue(0uL)
    }
}

class StopwatchViewModel : ViewModel() {
    private val _timer = MillisecondTimer()
    val timeText = _timer.time.map { formatTime(it) }
    val startStopButtonTextId = _timer.isActive.map { if (it) R.string.stop_button_text else R.string.start_button_text }
    private fun formatTime(timeMs: ULong): String {
        val minutes = timeMs / 1000uL / 60uL
        val seconds = (timeMs - minutes * 60uL * 1000uL) / 1000uL
        val ms = timeMs - minutes * 60uL * 1000uL - seconds * 1000uL
        return "%02d:%02d.%03d".format(minutes.toInt(), seconds.toInt(), ms.toInt())
    }

    fun onStartStopClicked() {
        if (_timer.isActive.value == true){
            _timer.stop()
        }else{
            _timer.start()
        }
    }

    fun onResetClicked() = _timer.reset()
}