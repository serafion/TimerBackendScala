package com.timerbackendscala.domain

sealed trait TimerCommand 

object TimerCommand {
  case class Start (id: TimerId) extends TimerCommand

  case class Pause(id: TimerId) extends TimerCommand

  case class Resume(id: TimerId) extends TimerCommand

  case class Stop(id: TimerId) extends TimerCommand

  case class Reset(id: TimerId) extends TimerCommand
  
  case class ElapsedMilliseconds(id: TimerId) extends TimerCommand
}
