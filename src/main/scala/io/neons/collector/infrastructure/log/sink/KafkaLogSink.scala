package io.neons.collector.infrastructure.log.sink

import java.util.UUID

import com.google.inject.Inject
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.model.log.{Log, LogSink}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{Future, Promise}

class KafkaLogSink @Inject()(kafkaProducer: KafkaProducer[UUID, Log], collectorConfig: CollectorConfig) extends LogSink {
  def sendToSink(log: Log): Future[String] = {
    val promise = Promise[String]()

    kafkaProducer.send(
      new ProducerRecord[UUID, Log](collectorConfig.sink.kafkaSinkConfig.topic, UUID.fromString(log.requestUuidL), log),
      (md: RecordMetadata, e: Exception) => {
        Option(md) match {
          case Some(x) => promise.success(x.toString)
          case None =>promise.failure(e)
        }
      }
    )

    promise.future
  }
}
