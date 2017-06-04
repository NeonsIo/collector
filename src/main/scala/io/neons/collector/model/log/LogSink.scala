package io.neons.collector.model.log

import scala.concurrent.Future

trait LogSink {
  def sendToSink(value: Log): Future[String]
}
