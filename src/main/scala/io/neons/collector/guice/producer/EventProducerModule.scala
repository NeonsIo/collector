package io.neons.collector.guice.producer

import java.util.{Properties, UUID}

import com.google.inject.{AbstractModule, Inject, Provider}
import io.neons.collector.config.CollectorConfig
import io.neons.collector.log.Log
import io.neons.collector.guice.producer.KafkaProducerModule.KafkaProducerModuleProvider
import io.neons.collector.producer.{LogProducer, KafkaLogProducer}
import net.codingwell.scalaguice.ScalaModule
import org.apache.kafka.clients.producer.KafkaProducer

object KafkaProducerModule {
  class KafkaProducerModuleProvider @Inject()(collectorConfig: CollectorConfig) extends Provider[KafkaProducer[UUID, Log]] {
    override def get(): KafkaProducer[UUID, Log] = {
      val kafkaConfig = collectorConfig.sink.kafkaSinkConfig
      val properties = new Properties()
      properties.put("bootstrap.servers", kafkaConfig.host.concat(":").concat(kafkaConfig.port.toString))
      properties.put("client.id", kafkaConfig.clientId)
      properties.put("acks", "all")
      properties.put("key.serializer", "io.neons.collector.guice.producer.serialization.UuidSerializer")
      properties.put("value.serializer", "io.neons.collector.guice.producer.serialization.RawEventSerializer")

      new KafkaProducer[UUID, Log](properties)
    }
  }
}

class EventProducerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[LogProducer].to[KafkaLogProducer].asEagerSingleton()
    bind[KafkaProducer[UUID, Log]].toProvider[KafkaProducerModuleProvider].asEagerSingleton()
  }
}
