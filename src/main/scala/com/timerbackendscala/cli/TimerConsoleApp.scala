package com.timerbackendscala.cli

import com.timerbackendscala.core.{SystemClock, TimerEngine}
import scala.language.postfixOps
import scala.language.experimental.macros

object TimerConsoleApp:

  def formatMs(ms: Long): String =
    val minutes = ms / 60000
    val seconds = (ms % 60000) / 1000
    val millis = ms % 1000
    f"$minutes%02d:$seconds%02d.$millis%03d"

  def main(args: Array[String]): Unit =
    println("🕒 Timer CLI - commands: start, pause, resume, stop, reset, elapsed, set <sec>, exit")
    val timerEngine = TimerEngine()(using SystemClock)
    var running = true
    var durationLimitMs: Option[Long] = None
    while running do
      print("> ")
      val input = scala.io.StdIn.readLine().trim.toLowerCase

      input match
        case "start" =>
          timerEngine.startTimer()
          println("✅ Timer started.")

        case "pause" =>
          timerEngine.pauseTimer()
          println("⏸ Timer paused.")

        case "resume" =>
          timerEngine.resumeTimer()
          println("▶️ Timer resumed.")

        case "stop" =>
          timerEngine.stopTimer()
          println("⏹ Timer stopped.")

        case "reset" =>
          timerEngine.resetTimer()
          println("🔄 Timer reset.")

        case "elapsed" =>
          val elapsed = timerEngine.elapsedMilliseconds()
          println(s"⏳ Elapsed: ${formatMs(elapsed)}")
          durationLimitMs.foreach { limit =>
            val remaining = math.max(0, limit - elapsed)
            println(s"⌛ Remaining: ${formatMs(remaining)}")
          }

        case cmd if cmd.startsWith("set ") =>
          val parts = cmd.split(" ")
          if parts.length == 2 && parts(1).forall(_.isDigit) then
            durationLimitMs = Some(parts(1).toLong * 1000)
            println(s"⏲ Timer limit set to ${parts(1)} seconds.")
          else
            println("⚠️  Usage: set <seconds>")

        case "exit" =>
          println("👋 Bye!")
          running = false

        case _ =>
          println("❓ Unknown command. Try: start, pause, resume, stop, reset, elapsed, set <sec>, exit")