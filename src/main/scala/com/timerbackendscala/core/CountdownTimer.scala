package com.timerbackendscala.core

case class CountdownTimer(
                           durationMs: Long,
                           timer: BasicTimer
                         )(using clock: Clock) extends BasicTimer(timer.state) {

  override def start(): CountdownTimer =
    if timer.state == NotStarted then
      copy(timer = timer.start())
    else this

  override def pause(): CountdownTimer =
    copy(timer = timer.pause())

  override def resume(): CountdownTimer =
    copy(timer = timer.resume())

  override def stop(): CountdownTimer =
    copy(timer = timer.stop())

  override def reset(): CountdownTimer =
    copy(timer = timer.reset())

  def isFinished: Boolean =
    remainingMs <= 0 && timer.isRunning

  override def isRunning: Boolean = timer.isRunning

  override def isPaused: Boolean = timer.isPaused

  def elapsedMs: Long = timer.elapsedMilliseconds()

  def remainingMs: Long =
    math.max(0, durationMs - elapsedMs)

  def currentState: String =
    if isFinished then "Finished"
    else if isPaused then "Paused"
    else if isRunning then "Running"
    else "NotStarted"
}
