package com.timerbackendscala.domain

import java.util.UUID

final case class TimerId(value: UUID)
object TimerId {
  def generate(): TimerId = TimerId(UUID.randomUUID())
}