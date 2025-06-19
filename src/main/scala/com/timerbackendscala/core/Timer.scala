package com.timerbackendscala.core

case class Timer(
                  state: TimerState = NotStarted
                )(using clock: Clock)
{
  def start(): Timer = {
    if (state == NotStarted) {
      copy(state = Running(clock.currentTimeMillis()))
    } else this
  }

  def pause(): Timer = {
    state match {
      case Running(startedAt) => copy(state = Paused(startedAt, clock.currentTimeMillis()))
      case _ => this
    }
  }

  def resume(): Timer = {
    state match {
      case Paused(startedAt, pausedAt) =>
        copy(state = Running(startedAt + (clock.currentTimeMillis() - pausedAt)))
      case _ => this
    }
  }

  def stop(): Timer = {
    state match {
      case Running(startedAt) => copy(state = Stopped(startedAt, clock.currentTimeMillis()))
      case Paused(startedAt, _) => copy(state = Stopped(startedAt, clock.currentTimeMillis()))
      case _ => this
    }
  }

  def reset(): Timer = copy(state = NotStarted)

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
}

