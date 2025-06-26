package com.timerbackendscala.core

class FakeClock(var currentTime: Long) extends Clock {
  override def currentTimeMillis(): Long = currentTime

  def advanceTimeBy(milliseconds: Long): Unit = {
    currentTime += milliseconds
  }
}
  
