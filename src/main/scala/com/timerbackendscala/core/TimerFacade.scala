package com.timerbackendscala.core

import com.timerbackendscala.domain.{TimerCommand, TimerEvent, TimerId}
import com.timerbackendscala.domain.timer.TimerKind

final case class TimerFacade(timers: Map[TimerId, TimerEngine] = Map.empty) {

  def createTimer(kind: TimerKind, name: String)(using clock: Clock): (TimerFacade, TimerId) = {
    val id = TimerId.generate()
    val engine = kind match {
      case TimerKind.Basic                  => TimerEngine()
      case TimerKind.Countdown(durationMs) => TimerEngine.countdown(durationMs)
    }
    (copy(timers = timers + (id -> engine)), id)
  }

  def handleCommand(id: TimerId, command: TimerCommand): (TimerFacade, TimerEvent) = {
    val engine = timers.getOrElse(id, throw new NoSuchElementException(s"No timer found with id: $id"))
    val updated = command match {
      case TimerCommand.Start(_)               => engine.startTimer()
      case TimerCommand.Pause(_)               => engine.pauseTimer()
      case TimerCommand.Resume(_)              => engine.resumeTimer()
      case TimerCommand.Stop(_)                => engine.stopTimer()
      case TimerCommand.Reset(_)               => engine.resetTimer()
      case TimerCommand.ElapsedMilliseconds(_) =>
        println(s"Elapsed ms: ${engine.elapsedMilliseconds()}")
        engine
    }
    val newFacade = copy(timers = timers.updated(id, updated))
    val event     = timerEvent(id, updated)
    (newFacade, event)
  }

  private def timerEvent(id: TimerId, timer: TimerEngine): TimerEvent = {
    timer.timer.state match {
      case NotStarted                => TimerEvent.Stopped(id)
      case Running(startedAt)        => TimerEvent.Started(id)
      case Paused(startedAt, pause)  => TimerEvent.Paused(id)
      case Stopped(startedAt, ended) => TimerEvent.Stopped(id)
    }
  }
}
