package io.neons.collector.producer

import io.neons.collector.log.Log

trait LogProducer {
  def produce(value: Log): Any
}
