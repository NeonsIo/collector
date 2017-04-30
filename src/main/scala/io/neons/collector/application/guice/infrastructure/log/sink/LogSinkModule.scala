package io.neons.collector.application.guice.infrastructure.log.sink

import java.util.{Properties, UUID}

import com.google.inject.{AbstractModule, Inject, Provider}
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.application.guice.infrastructure.log.sink.LogSinkModule.KafkaLogSinkModuleProvider
import io.neons.collector.infrastructure.log.sink.KafkaLogSink
import io.neons.collector.model.log.{Log, LogSink}
import net.codingwell.scalaguice.ScalaModule
import org.apache.kafka.clients.producer.KafkaProducer

object LogSinkModule {
  class KafkaLogSinkModuleProvider @Inject()(collectorConfig: CollectorConfig) extends Provider[KafkaProducer[UUID, Log]] {
    override def get(): KafkaProducer[UUID, Log] = {
      val kafkaConfig = collectorConfig.sink.kafkaSinkConfig
      val properties = new Properties()
      properties.put("bootstrap.servers", kafkaConfig.host.concat(":").concat(kafkaConfig.port.toString))
      properties.put("client.id", kafkaConfig.clientId)
      properties.put("acks", "all")
      properties.put("key.serializer", "io.neons.collector.application.guice.infrastructure.log.sink.serialization.UuidSerializer")
      properties.put("value.serializer", "io.neons.collector.application.guice.infrastructure.log.sink.serialization.RawEventSerializer")

      new KafkaProducer[UUID, Log](properties)
    }
  }
}

class LogSinkModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[LogSink].to[KafkaLogSink].asEagerSingleton()
    bind[KafkaProducer[UUID, Log]].toProvider[KafkaLogSinkModuleProvider].asEagerSingleton()
  }
}
