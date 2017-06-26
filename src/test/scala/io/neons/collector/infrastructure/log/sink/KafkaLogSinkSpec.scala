package io.neons.collector.infrastructure.log.sink

import java.util.UUID

import io.neons.collector.model.log.{Log, LogHeaderBag}
import io.neons.collector.testcase.CollectorConfigTestCase
import org.apache.kafka.clients.producer.KafkaProducer
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class KafkaLogSinkSpec extends FlatSpec with Matchers with MockitoSugar with CollectorConfigTestCase {
  "Kafka log sink" should "produce log to kafka " in {
      val kafkaProducerMock = mock[KafkaProducer[UUID, Log]]

    val producer = new KafkaLogSink(kafkaProducerMock, collectorConfig)
      val log = Log(
        "3ec0415c-401a-4936-83a0-f21723d0f38a",
        "GET",
        "http://neons.io",
        new ListBuffer[LogHeaderBag],
        new ListBuffer[LogHeaderBag],
        "127.0.0.1",
        1493239462820L
      )
      producer.sendToSink(log)
  }
}
