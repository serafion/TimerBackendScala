package com.timerbackendscala.core

case class CountdownTimer(
                           durationMs: Long,
                           timer: Timer
                         )(using clock: Clock) {

  def start(): CountdownTimer =
    if timer.state == NotStarted then
      copy(timer = timer.start())
    else this

  def pause(): CountdownTimer =
    copy(timer = timer.pause())

  def resume(): CountdownTimer =
    copy(timer = timer.resume())

  def stop(): CountdownTimer =
    copy(timer = timer.stop())

  def reset(): CountdownTimer =
    copy(timer = timer.reset())

  def isFinished: Boolean =
    remainingMs <= 0 && timer.isRunning

  def isRunning: Boolean = timer.isRunning

  def isPaused: Boolean = timer.isPaused

  def elapsedMs: Long = timer.elapsedMilliseconds()

  def remainingMs: Long =
    math.max(0, durationMs - elapsedMs)

  def currentState: String =
    if isFinished then "Finished"
    else if isPaused then "Paused"
    else if isRunning then "Running"
    else "NotStarted"
}
