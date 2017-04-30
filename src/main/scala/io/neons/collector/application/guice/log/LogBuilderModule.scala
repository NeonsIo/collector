package io.neons.collector.application.guice.log

import com.google.inject.AbstractModule
import io.neons.collector.infrastructure.log.LogBuilderImpl
import io.neons.collector.model.log.LogBuilder
import net.codingwell.scalaguice.ScalaModule

class LogBuilderModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[LogBuilder].to[LogBuilderImpl].asEagerSingleton()
  }
}
