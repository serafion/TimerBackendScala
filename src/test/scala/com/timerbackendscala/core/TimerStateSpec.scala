package com.timerbackendscala.core

import org.scalatest.funsuite.AnyFunSuiteLike

class TimerStateSpec extends AnyFunSuiteLike {
  test("NotStarted should be a TimerState") {
    assert(NotStarted.isInstanceOf[TimerState])
  }

  test("Running should be a TimerState") {
    val runningState = Running(1000L)
    assert(runningState.isInstanceOf[TimerState])
    assert(runningState.startedAt == 1000L)
  }

  test("Paused should be a TimerState") {
    val pausedState = Paused(1000L, 2000L)
    assert(pausedState.isInstanceOf[TimerState])
    assert(pausedState.startedAt == 1000L)
    assert(pausedState.pausedAt == 2000L)
  }

  test("Stopped should be a TimerState") {
    val stoppedState = Stopped(1000L, 2000L)
    assert(stoppedState.isInstanceOf[TimerState])
    assert(stoppedState.startedAt == 1000L)
    assert(stoppedState.endedAt == 2000L)
  }
}
