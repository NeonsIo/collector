package io.neons.collector.guice.repository

import java.util.Properties
import com.google.inject.{AbstractModule, Inject, Provider}
import io.neons.collector.config.CollectorConfig
import io.neons.collector.guice.repository.KafkaProducerModule.KafkaProducerModuleProvider
import io.neons.collector.repository.{EventRepository, KafkaEventRepository}
import net.codingwell.scalaguice.ScalaModule
import org.apache.kafka.clients.producer.KafkaProducer

object KafkaProducerModule {
  class KafkaProducerModuleProvider @Inject()(collectorConfig: CollectorConfig) extends Provider[KafkaProducer[String, String]] {
    override def get(): KafkaProducer[String, String] = {
      val kafkaConfig = collectorConfig.sink.kafkaSinkConfig
      val properties = new Properties()
      properties.put("bootstrap.servers", kafkaConfig.host.concat(":").concat(kafkaConfig.port.toString))
      properties.put("client.id", kafkaConfig.clientId)
      properties.put("acks", "all")
      properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

      new KafkaProducer[String, String](properties)
    }
  }
}

class EventRepositoryModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[EventRepository].to[KafkaEventRepository].asEagerSingleton()
    bind[KafkaProducer[String, String]].toProvider[KafkaProducerModuleProvider].asEagerSingleton()
  }
}
