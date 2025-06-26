package com.timerbackendscala.core.timers

import com.timerbackendscala.core.{Clock, NotStarted, Timer, TimerState}

class CountdownTimer(
                      val durationMs: Long,
                      val timer: BasicTimer
                    )(using clock: Clock) extends Timer {

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

  def isRunning: Boolean = timer.isRunning

  def isPaused: Boolean = timer.isPaused

  def remainingMs: Long =
    math.max(0, durationMs - elapsedMilliseconds())

  def isFinished: Boolean =
    remainingMs <= 0 && timer.isRunning

  override def elapsedMilliseconds(): Long = timer.elapsedMilliseconds()

  override def state: TimerState = timer.state

  def copy(
            durationMs: Long = this.durationMs,
            timer: BasicTimer = this.timer
          ): CountdownTimer =
    new CountdownTimer(durationMs, timer)
}

object CountdownTimer:
  def apply(durationMs: Long)(using clock: Clock): CountdownTimer =
    new CountdownTimer(durationMs, BasicTimer()(using clock))
  def apply(durationMs: Long, timer: BasicTimer)(using clock: Clock): CountdownTimer =
    new CountdownTimer(durationMs, timer)