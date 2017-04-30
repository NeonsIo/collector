package io.neons.collector.infrastructure.log.sink

import java.util.UUID

import com.google.inject.Inject
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.model.log.{Log, LogSink}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaLogSink @Inject()(kafkaProducer: KafkaProducer[UUID, Log], collectorConfig: CollectorConfig) extends LogSink {
  def sendToSink(log: Log): Any = {
    kafkaProducer.send(
      new ProducerRecord[UUID, Log](collectorConfig.sink.kafkaSinkConfig.topic, UUID.fromString(log.requestUuidL), log)
    )
  }
}
