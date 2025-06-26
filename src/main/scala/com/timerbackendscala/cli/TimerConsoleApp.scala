package com.timerbackendscala.cli

import com.timerbackendscala.core.{SystemClock, TimerEngine, TimerFacade}
import com.timerbackendscala.domain.{TimerCommand, TimerId}
import com.timerbackendscala.domain.TimerKind
import com.timerbackendscala.domain.TimerKind.{Basic, Countdown}
import com.timerbackendscala.core.SystemClock
import com.timerbackendscala.core.Clock
import com.timerbackendscala.domain.TimerCommand.{ElapsedMilliseconds, Pause, Reset, Resume, Start, Stop}

import scala.annotation.tailrec
import java.util.UUID
import scala.io.StdIn

object TimerConsoleApp:

  def main(args: Array[String]): Unit =
    println("Welcome to the Timer Console App!")
    println("Available commands: start, pause, resume, stop, reset, elapsed, list, exit")

    loop(TimerFacade())

  @tailrec
  def loop(facade: TimerFacade): Unit =
    given Clock = SystemClock
    print("Enter command: ")
    val input = StdIn.readLine().trim.toLowerCase

    input match
      case "create" =>
        val kind = promptTimerKind()
        val (updatedFacade, timerId) = facade.createTimer(kind, "My Timer")
        println(s"Timer created with ID: $timerId")
        loop(updatedFacade)

      case "start" |"pause" | "resume" | "stop" | "reset" | "elapsed" =>
        val timerId = TimerId(UUID.fromString(StdIn.readLine("Enter timer ID: ").trim))
        val command = fromString(input, timerId)
        command match
          case Some(cmd) =>
            val (updatedFacade, event) = facade.handleCommand(timerId, cmd)
            println(s"Command executed: $event")
            loop(updatedFacade)
          case None =>
            println("Invalid command.")
            loop(facade)

      case "list" =>
        println("Listing all timers:")
        facade.timers.foreach { case (id, engine) =>
          println(s"ID: $id, State: ${engine.currentState}")
        }
        loop(facade)

      case "exit" =>
        println("Thank you for using the Timer Console App!")

      case _ =>
        println("Unknown command.")
        loop(facade)

  def promptTimerKind(): TimerKind =
    println("Enter timer kind (basic/countdown): ")
    StdIn.readLine().trim.toLowerCase match
      case "basic" => TimerKind.Basic
      case "countdown" =>
        println("Enter countdown duration in milliseconds: ")
        val duration = StdIn.readLine().trim.toLongOption.getOrElse(1000L)
        TimerKind.Countdown(duration)
      case _ =>
        println("Invalid timer kind. Defaulting to Basic.")
        TimerKind.Basic

  def fromString(input: String, timerId: TimerId): Option[TimerCommand] =
    input match
      case "start"               => Some(Start(timerId))
      case "pause"               => Some(Pause(timerId))
      case "resume"              => Some(Resume(timerId))
      case "stop"                => Some(Stop(timerId))
      case "reset"               => Some(Reset(timerId))
      case "elapsed"             => Some(ElapsedMilliseconds(timerId))
      case _                     => None

extension (sc: StdIn.type)
  def readLine(prompt: String): String =
    print(prompt)
    sc.readLine()


