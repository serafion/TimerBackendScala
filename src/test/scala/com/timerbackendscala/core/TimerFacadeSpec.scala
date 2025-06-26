package com.timerbackendscala.core

import com.timerbackendscala.core.timers.{BasicTimer, CountdownTimer}
import com.timerbackendscala.domain.TimerCommand
import com.timerbackendscala.domain.TimerKind.{Basic, Countdown}
import org.scalatest.funsuite.AnyFunSuiteLike

class TimerFacadeSpec extends AnyFunSuiteLike {
  // write tests for TimerFacade here start with a simple test to ensure the facade can be created
  test("TimerFacade can be created") {
    val facade = TimerFacade()
    assert(facade != null)
  }
  test("TimerFacade can create BasicTimer") {
    val facade = TimerFacade()
    val (facadeWithTimer, timerId) = facade.createTimer(Basic, "Basic")(using FakeClock(0L))
    assert(facadeWithTimer.timers.contains(timerId))
    assert(facadeWithTimer.timers(timerId).timer.isInstanceOf[BasicTimer])
  }

  test("TimerFacade can create CountdownTimer") {
    val facade = TimerFacade()
    val (facadeWithTimer, timerId) = facade.createTimer(Countdown(1000L), "countdown")(using FakeClock(0L))
    assert(facadeWithTimer.timers.contains(timerId))
    assert(facadeWithTimer.timers(timerId).timer.isInstanceOf[CountdownTimer])
  }
  //write a test to ensure that all TimerCommands can be executed on the facade with BasicTimer and CountdownTimer use handle command method and TimerCommand
  test("TimerFacade can handle TimerCommands for BasicTimer") {
    val facade = TimerFacade()
    val (facadeWithTimer, timerId) = facade.createTimer(Basic, "Basic")(using FakeClock(0L))
    val command = TimerCommand.Start(timerId)
    val updatedFacade = facadeWithTimer.handleCommand(timerId, command)
    assert(updatedFacade._1.timers(timerId).timer.isRunning)

    val pauseCommand = TimerCommand.Pause(timerId)
    val pausedFacade = updatedFacade._1.handleCommand(timerId, pauseCommand)
    assert(pausedFacade._1.timers(timerId).timer.isPaused)

    val resumeCommand = TimerCommand.Resume(timerId)
    val resumedFacade = pausedFacade._1.handleCommand(timerId, resumeCommand)
    assert(resumedFacade._1.timers(timerId).timer.isRunning)

    val stopCommand = TimerCommand.Stop(timerId)
    val stoppedFacade = resumedFacade._1.handleCommand(timerId, stopCommand)
    assert(stoppedFacade._1.timers(timerId).timer.state.isInstanceOf[Stopped])
  }
}
