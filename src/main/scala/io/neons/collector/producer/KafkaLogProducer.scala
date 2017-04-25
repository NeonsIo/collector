package io.neons.collector.producer

import  java.util.UUID

import com.google.inject.Inject
import io.neons.collector.config.CollectorConfig
import io.neons.collector.log.Log
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaLogProducer @Inject()(kafkaProducer: KafkaProducer[UUID, Log], collectorConfig: CollectorConfig) extends LogProducer {
  def produce(log: Log): Any = {
    kafkaProducer.send(
      new ProducerRecord[UUID, Log](collectorConfig.sink.kafkaSinkConfig.topic, UUID.fromString(log.requestUuidL), log)
    )
  }
}
