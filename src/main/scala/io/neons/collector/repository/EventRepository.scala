package io.neons.collector.repository

import scala.concurrent.Future

trait EventRepository {
  def add(uuid: String, value: String): Future[Boolean]
}
