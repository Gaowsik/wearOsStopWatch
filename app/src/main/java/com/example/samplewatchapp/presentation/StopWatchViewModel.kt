package com.example.samplewatchapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class StopWatchViewModel : ViewModel() {

    private val _elapsedTime = MutableStateFlow(0L)
    private val _timerState = MutableStateFlow(TimeState.RESET)
    val timerState = _timerState.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS")
    val stopWatchText = _elapsedTime.map { mills ->
        LocalTime.ofNanoOfDay(mills * 1_000_000).format(formatter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00:00:000")

    init {
        _timerState.flatMapLatest { timerState ->
            getTimerFlow(isRunning = timerState == TimeState.RUNNING)

        }.onEach { timeDiff ->
            _elapsedTime.update { it + timeDiff }

        }.launchIn(viewModelScope)
    }


    fun toggleRunning() {
        when (timerState.value) {
            TimeState.RUNNING -> _timerState.update { TimeState.PAUSED }
            TimeState.PAUSED -> _timerState.update { TimeState.RUNNING }
            TimeState.RESET -> _timerState.update { TimeState.RUNNING }
        }
    }

    fun resetTimer(){
        _timerState.update { TimeState.RESET }
        _elapsedTime.update { 0L }

    }

    private fun getTimerFlow(isRunning: Boolean): Flow<Long> {

        return flow {
            var startMills = System.currentTimeMillis()
            while (isRunning) {
                val currentMills = System.currentTimeMillis()
                val timeDiff = if (currentMills > startMills) {
                    currentMills - startMills
                } else 0L

                emit(timeDiff)

                startMills = System.currentTimeMillis()
                delay(10L)


            }
        }

    }

}