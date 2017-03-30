package io.neons.collector.producer

import java.util.UUID

import com.google.inject.Inject
import io.neons.collector.config.CollectorConfig
import io.neons.collector.event.Event
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaEventProducer @Inject()(kafkaProducer: KafkaProducer[UUID, Event], collectorConfig: CollectorConfig) extends EventProducer {
  def produce(event: Event): Any = {
    kafkaProducer.send(
      new ProducerRecord[UUID, Event](collectorConfig.sink.kafkaSinkConfig.topic, UUID.fromString(event.requestUuidL), event)
    )
  }
}
