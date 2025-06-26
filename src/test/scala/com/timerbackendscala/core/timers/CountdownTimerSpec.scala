package com.timerbackendscala.core.timers

import com.timerbackendscala.core.Clock
import com.timerbackendscala.core.timers.{BasicTimer, CountdownTimer}
import org.scalatest.funsuite.AnyFunSuiteLike

class CountdownTimerSpec extends AnyFunSuiteLike {
  class FakeClock(private var currentTime: Long) extends Clock {
    override def currentTimeMillis(): Long = synchronized {
      currentTime
    }

    def advanceTimeBy(milliseconds: Long): Unit = synchronized {
      currentTime += milliseconds
    }
  }

  test("start transitions CountdownTimer to Running state") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock)
    val startedTimer = countdownTimer.start()
    assert(startedTimer.isRunning)
    assert(startedTimer.currentState == "Running")
  }

  test("pause transitions CountdownTimer to Paused state") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(1000L)
    val pausedTimer = countdownTimer.pause()
    assert(pausedTimer.isPaused)
    assert(pausedTimer.currentState == "Paused")
  }

  test("resume transitions CountdownTimer to Running state from Paused") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(1000L)
    val pausedTimer = countdownTimer.pause()
    fakeClock.advanceTimeBy(1000L)
    val resumedTimer = pausedTimer.resume()
    assert(resumedTimer.isRunning)
    assert(resumedTimer.currentState == "Running")
  }

  test("stop transitions CountdownTimer to Stopped state") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(1000L)
    val stoppedTimer = countdownTimer.stop()
    assert(!stoppedTimer.isRunning)
    assert(stoppedTimer.currentState == "NotStarted")
  }

  test("reset transitions CountdownTimer to NotStarted state") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    val resetTimer = countdownTimer.reset()
    assert(!resetTimer.isRunning)
    assert(resetTimer.currentState == "NotStarted")
  }

  test("isFinished returns true when remainingMs is 0 and timer is running") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(2000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(2000L)
    assert(countdownTimer.isFinished)
    assert(countdownTimer.currentState == "Finished")

  }

  test("remainingMs returns correct remaining time") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(2000L)
    assert(countdownTimer.remainingMs == 3000L)
  }

  test("elapsedMs returns correct elapsed time") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(2000L)
    assert(countdownTimer.elapsedMilliseconds() == 2000L)
  }

  test("currentState returns Finished when timer is finished") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(2000L, BasicTimer()(using fakeClock))(using fakeClock).start()
    fakeClock.advanceTimeBy(2000L)
    assert(countdownTimer.currentState == "Finished")
  }

  test("currentState returns NotStarted when timer is not started") {
    val fakeClock = new FakeClock(1000L)
    val countdownTimer = CountdownTimer(5000L, BasicTimer()(using fakeClock))(using fakeClock)
    assert(countdownTimer.currentState == "NotStarted")
  }
}