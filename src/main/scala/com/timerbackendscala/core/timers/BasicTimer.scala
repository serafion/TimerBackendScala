package com.timerbackendscala.core.timers

import com.timerbackendscala.core.*

class BasicTimer(
                  private val timerState: TimerState = NotStarted
                )(using clock: Clock) extends Timer:

  def start(): BasicTimer =
    if state == NotStarted then
      BasicTimer(Running(clock.currentTimeMillis()))
    else this

  def pause(): BasicTimer =
    state match
      case Running(startedAt) =>
        BasicTimer(Paused(startedAt, clock.currentTimeMillis()))
      case _ => this

  def resume(): BasicTimer =
    state match
      case Paused(startedAt, pausedAt) =>
        BasicTimer(Running(startedAt + (clock.currentTimeMillis() - pausedAt)))
      case _ => this

  def stop(): BasicTimer =
    state match
      case Running(startedAt) =>
        BasicTimer(Stopped(startedAt, clock.currentTimeMillis()))
      case Paused(startedAt, _) =>
        BasicTimer(Stopped(startedAt, clock.currentTimeMillis()))
      case _ => this

  def reset(): BasicTimer = BasicTimer(NotStarted)

  def elapsedMilliseconds(): Long =
    state match
      case Running(startedAt)           => clock.currentTimeMillis() - startedAt
      case Paused(startedAt, pausedAt)  => pausedAt - startedAt
      case Stopped(startedAt, endedAt)  => endedAt - startedAt
      case NotStarted                   => 0L

  def isRunning: Boolean = state.isInstanceOf[Running]

  def isPaused: Boolean = state.isInstanceOf[Paused]

  override def state: TimerState = timerState

object BasicTimer:
  def apply()(using clock: Clock): BasicTimer = new BasicTimer(NotStarted)
  def apply(state: TimerState)(using clock: Clock): BasicTimer = new BasicTimer(state)
