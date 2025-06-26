package com.timerbackendscala.core.timers

import com.timerbackendscala.core.timers.BasicTimer
import com.timerbackendscala.core.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class BasicTimerSpec extends AnyFlatSpec with Matchers {
  implicit val clock: Clock = SystemClock

  "A Timer" should "start in NotStarted state" in {
    val timer = BasicTimer()
    timer.state shouldBe NotStarted
  }

  it should "transition to Running state when started" in {
    val timer = BasicTimer().start()
    timer.state shouldBe a[Running]
  }

  it should "transition to Paused state when paused" in {
    val timer = BasicTimer().start().pause()
    timer.state shouldBe a[Paused]
  }

  it should "transition to Running state when resumed" in {
    val timer = BasicTimer().start().pause().resume()
    timer.state shouldBe a[Running]
  }

  it should "transition to Stopped state when stopped" in {
    val timer = BasicTimer().start().stop()
    timer.state shouldBe a[Stopped]
  }

  it should "reset to NotStarted state when reset" in {
    val timer = BasicTimer().start().reset()
    timer.state shouldBe NotStarted
  }
}
