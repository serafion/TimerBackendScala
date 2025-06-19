package com.timerbackendscala.core

trait TimerState {

}

case object NotStarted extends TimerState

case class Running(startedAt: Long) extends TimerState

case class Paused(startedAt: Long, pausedAt: Long) extends TimerState

case class Stopped(startedAt: Long, endedAt: Long) extends TimerState