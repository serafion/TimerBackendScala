package com.timerbackendscala.core

class TimerEngine(val timer: Timer) {

  def startTimer(): TimerEngine =
    TimerEngine(timer.start())

  def pauseTimer(): TimerEngine =
    TimerEngine(timer.pause())

  def resumeTimer(): TimerEngine =
    TimerEngine(timer.resume())

  def stopTimer(): TimerEngine =
    TimerEngine(timer.stop())

  def resetTimer(): TimerEngine =
    TimerEngine(timer.reset())

  def elapsedMilliseconds(): Long =
    timer.elapsedMilliseconds()

  def isRunning: Boolean =
    timer.isRunning

  def isPaused: Boolean =
    timer.isPaused

  def currentState: TimerState =
    timer.state

  def getTimer: Timer = timer
}

object TimerEngine {
  def apply()(using clock: Clock): TimerEngine =
    new TimerEngine(BasicTimer())

  def apply(timer: Timer): TimerEngine =
    new TimerEngine(timer)  

  def countdown(durationMs: Long)(using clock: Clock): TimerEngine =
    new TimerEngine(new CountdownTimer(durationMs, BasicTimer()))
}
