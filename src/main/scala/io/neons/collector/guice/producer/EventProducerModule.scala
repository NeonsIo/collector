package io.neons.collector.guice.producer

import java.util.{Properties, UUID}

import com.google.inject.{AbstractModule, Inject, Provider}
import io.neons.collector.config.CollectorConfig
import io.neons.collector.event.Event
import io.neons.collector.guice.producer.KafkaProducerModule.KafkaProducerModuleProvider
import io.neons.collector.producer.{EventProducer, KafkaEventProducer}
import net.codingwell.scalaguice.ScalaModule
import org.apache.kafka.clients.producer.KafkaProducer

object KafkaProducerModule {
  class KafkaProducerModuleProvider @Inject()(collectorConfig: CollectorConfig) extends Provider[KafkaProducer[UUID, Event]] {
    override def get(): KafkaProducer[UUID, Event] = {
      val kafkaConfig = collectorConfig.sink.kafkaSinkConfig
      val properties = new Properties()
      properties.put("bootstrap.servers", kafkaConfig.host.concat(":").concat(kafkaConfig.port.toString))
      properties.put("client.id", kafkaConfig.clientId)
      properties.put("acks", "all")
      properties.put("key.serializer", "io.neons.collector.guice.producer.serialization.UuidSerializer")
      properties.put("value.serializer", "io.neons.collector.guice.producer.serialization.RawEventSerializer")

      new KafkaProducer[UUID, Event](properties)
    }
  }
}

class EventProducerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[EventProducer].to[KafkaEventProducer].asEagerSingleton()
    bind[KafkaProducer[UUID, Event]].toProvider[KafkaProducerModuleProvider].asEagerSingleton()
  }
}
