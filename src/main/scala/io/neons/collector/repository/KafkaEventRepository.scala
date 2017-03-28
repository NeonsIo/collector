package io.neons.collector.repository

import com.google.inject.Inject
import io.neons.collector.config.CollectorConfig
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaEventRepository @Inject()(kafkaProducer: KafkaProducer[String, String], collectorConfig: CollectorConfig) extends EventRepository {
  def add(uuid: String, value: String): Any = {
      val data = new ProducerRecord[String, String](collectorConfig.sink.kafkaSinkConfig.topic, uuid.toString, value)
      kafkaProducer.send(data)
  }
}
