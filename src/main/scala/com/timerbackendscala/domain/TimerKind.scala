package com.timerbackendscala.domain

sealed trait TimerKind

object TimerKind {
  case object Basic extends TimerKind

  final case class Countdown(durationMs: Long) extends TimerKind {
    require(durationMs > 0, "Duration must be greater than zero")
  }
}
