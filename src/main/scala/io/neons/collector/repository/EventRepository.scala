package io.neons.collector.repository

trait EventRepository {
  def add(uuid: String, value: String): Any
}
