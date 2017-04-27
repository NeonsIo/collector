package io.neons.collector.producer

import java.util.UUID

import io.neons.collector.log.{HeaderBag, Log}
import io.neons.testcase.CollectorConfigTestCase
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Mockito._
import scala.collection.mutable.ListBuffer

class KafkaProducerSpec  extends FlatSpec with Matchers with MockitoSugar with CollectorConfigTestCase {
  "Kafka producer" should "produce log to kafka " in {
      val kafkaProducerMock = mock[KafkaProducer[UUID, Log]]
      val producer = new KafkaLogProducer(kafkaProducerMock, collectorConfig)
      val log = Log(
        "3ec0415c-401a-4936-83a0-f21723d0f38a",
        "GET",
        "http://neons.io",
        new ListBuffer[HeaderBag],
        new ListBuffer[HeaderBag],
        "127.0.0.1",
        1493239462820L
      )
      producer.produce(log)

      verify(kafkaProducerMock).send(
        new ProducerRecord[UUID, Log]("neons", UUID.fromString("3ec0415c-401a-4936-83a0-f21723d0f38a"), log)
      )
  }
}
