package com.timerbackendscala.core

class BasicTimer(
                  timerState: TimerState = NotStarted
                )(using clock: Clock) extends Timer
{
  def start(): BasicTimer = {
    if (state == NotStarted) {
      BasicTimer(state = Running(clock.currentTimeMillis()))
    } else this
  }

  def pause(): BasicTimer = {
    state match {
      case Running(startedAt) => BasicTimer(state = Paused(startedAt, clock.currentTimeMillis()))
      case _ => this
    }
  }

  def resume(): BasicTimer = {
    state match {
      case Paused(startedAt, pausedAt) =>
        BasicTimer(state = Running(startedAt + (clock.currentTimeMillis() - pausedAt)))
      case _ => this
    }
  }

  def stop(): BasicTimer = {
    state match {
      case Running(startedAt) => BasicTimer(state = Stopped(startedAt, clock.currentTimeMillis()))
      case Paused(startedAt, _) => BasicTimer(state = Stopped(startedAt, clock.currentTimeMillis()))
      case _ => this
    }
  }

  def reset(): BasicTimer = BasicTimer(state = NotStarted)

  def elapsedMilliseconds(): Long = {
    state match {
      case Running(startedAt) => clock.currentTimeMillis() - startedAt
      case Paused(startedAt, pausedAt) => pausedAt - startedAt
      case Stopped(startedAt, endedAt) => endedAt - startedAt
      case NotStarted => 0L
    }
  }

  def isRunning: Boolean = state match {
    case Running(_) => true
    case _ => false
  }

  def isPaused: Boolean = state match {
    case Paused(_, _) => true
    case _ => false
  }

  def state: TimerState = timerState
}

object BasicTimer:
  def apply()(using clock: Clock): BasicTimer = new BasicTimer()

  def apply(state: TimerState)(using clock: Clock): BasicTimer = new BasicTimer(state)
end BasicTimer

