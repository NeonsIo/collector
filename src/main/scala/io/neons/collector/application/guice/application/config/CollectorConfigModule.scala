package io.neons.collector.application.guice.application.config

import com.google.inject.{AbstractModule, Provider}
import com.typesafe.config.ConfigFactory
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.application.guice.application.config.CollectorConfigModule.CollectorConfigProvider
import net.codingwell.scalaguice.ScalaModule

object CollectorConfigModule {
  class CollectorConfigProvider extends Provider[CollectorConfig] {
    override def get(): CollectorConfig = {
      CollectorConfig.load(ConfigFactory.load())
    }
  }
}

class CollectorConfigModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CollectorConfig].toProvider[CollectorConfigProvider].asEagerSingleton()
  }
}
