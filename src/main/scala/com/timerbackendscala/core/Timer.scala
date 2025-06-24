package com.timerbackendscala.core

trait Timer {
  def start(): Timer
  def pause(): Timer
  def resume(): Timer
  def stop(): Timer
  def reset(): Timer
  def elapsedMilliseconds(): Long
  def isRunning: Boolean
  def isPaused: Boolean
  def state: TimerState
}
