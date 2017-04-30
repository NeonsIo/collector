package io.neons.collector.model.log

trait LogSink {
  def sendToSink(value: Log): Any
}
