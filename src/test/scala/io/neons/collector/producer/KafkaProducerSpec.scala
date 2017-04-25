package io.neons.collector.producer

import java.util.UUID

import io.neons.collector.log.Log
import org.apache.kafka.clients.producer.KafkaProducer
import org.scalatest.{Matchers, WordSpec}
import org.scalamock.scalatest.MockFactory

class KafkaProducerSpec  extends WordSpec with Matchers with MockFactory {
  "Kafka producer" should {
    "produce log to kafka " in {
      val kafkaProducerMock = mock[KafkaProducer[UUID, Log]]
//      (kafkaProducerMock send _).expects()
    }
  }
}
