package com.timerbackendscala.core

class TimerEngine()(using clock: Clock) {

  private var timer: BasicTimer = BasicTimer()(using clock)

  def startTimer(): Unit = {
    timer = timer.start()
  }

  def pauseTimer(): Unit = {
    timer = timer.pause()
  }

  def resumeTimer(): Unit = {
    timer = timer.resume()
  }

  def stopTimer(): Unit = {
    timer = timer.stop()
  }

  def resetTimer(): Unit = {
    timer = timer.reset()
  }

  def elapsedMilliseconds(): Long = {
    timer.elapsedMilliseconds()
  }

  def isRunning: Boolean = {
    timer.isRunning
  }

  def isPaused: Boolean = {
    timer.isPaused
  }

  def currentState: TimerState = {
    timer.state
  }

  def getTimer: BasicTimer = {
    timer
  }
}
