package io.neons.collector.application.guice.infrastructure.log.builder

import com.google.inject.AbstractModule
import io.neons.collector.infrastructure.log.builder.AkkaHttpLogBuilder
import io.neons.collector.model.log.LogBuilder
import net.codingwell.scalaguice.ScalaModule

class LogBuilderModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[LogBuilder].to[AkkaHttpLogBuilder].asEagerSingleton()
  }
}
