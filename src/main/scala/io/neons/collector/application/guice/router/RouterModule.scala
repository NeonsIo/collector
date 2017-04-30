package io.neons.collector.application.guice.router

import com.google.inject.AbstractModule
import io.neons.collector.application.router.{Router, CollectorRouter}
import net.codingwell.scalaguice.ScalaModule

class RouterModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Router].to[CollectorRouter].asEagerSingleton()
  }
}
