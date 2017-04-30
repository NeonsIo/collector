package io.neons.collector.model.log

trait LogProducer {
  def produce(value: Log): Any
}
