package io.neons.collector.producer

import io.neons.collector.event.Event

trait EventProducer {
  def produce(value: Event): Any
}
