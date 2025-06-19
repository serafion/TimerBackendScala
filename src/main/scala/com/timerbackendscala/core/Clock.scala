package com.timerbackendscala.core

trait Clock {
  def currentTimeMillis(): Long
}

object SystemClock extends Clock {
  override def currentTimeMillis(): Long = System.currentTimeMillis()
}
