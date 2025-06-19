package com.timerbackendscala.core

import org.scalatest.funsuite.AnyFunSuiteLike

class TimerEngineSpec extends AnyFunSuiteLike {
  class FakeClock(var currentTime: Long) extends Clock {
    override def currentTimeMillis(): Long = currentTime
    def advanceTimeBy(milliseconds: Long): Unit = {
      currentTime += milliseconds
    }
  }

  test("startTimer transitions TimerEngine to Running state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    assert(engine.currentState == Running(1000L))
  }

  test("pauseTimer transitions TimerEngine to Paused state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    fakeClock.advanceTimeBy(1000L)
    engine.pauseTimer()
    assert(engine.currentState == Paused(1000L, 2000L))
  }

  test("resumeTimer transitions TimerEngine to Running state from Paused") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    fakeClock.advanceTimeBy(1000L)
    engine.pauseTimer()
    fakeClock.advanceTimeBy(1000L)
    engine.resumeTimer()
    assert(engine.currentState == Running(2000L))
  }

  test("stopTimer transitions TimerEngine to Stopped state from Running") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    fakeClock.advanceTimeBy(1000L)
    engine.stopTimer()
    assert(engine.currentState == Stopped(1000L, 2000L))
  }

  test("resetTimer transitions TimerEngine to NotStarted state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    engine.resetTimer()
    assert(engine.currentState == NotStarted)
  }

  test("elapsedMilliseconds returns correct time for Running state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    fakeClock.advanceTimeBy(2000L)
    assert(engine.elapsedMilliseconds() == 2000L)
  }

  test("elapsedMilliseconds returns 0 for NotStarted state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    assert(engine.elapsedMilliseconds() == 0L)
  }

  test("isRunning returns true if TimerEngine is in Running state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    assert(engine.isRunning)
  }

  test("isRunning returns false if TimerEngine is not in Running state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    assert(!engine.isRunning)
  }

  test("isPaused returns true if TimerEngine is in Paused state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    engine.startTimer()
    fakeClock.advanceTimeBy(1000L)
    engine.pauseTimer()
    assert(engine.isPaused)
  }

  test("isPaused returns false if TimerEngine is not in Paused state") {
    val fakeClock = new FakeClock(1000L)
    val engine = TimerEngine()(using fakeClock)
    assert(!engine.isPaused)
  }
}