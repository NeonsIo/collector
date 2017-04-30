package io.neons.collector.application.guice.application.akka.http.router

import com.google.inject.AbstractModule
import io.neons.collector.application.akka.http.router.{Router, ApplicationRouter}
import net.codingwell.scalaguice.ScalaModule

class RouterModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Router].to[ApplicationRouter].asEagerSingleton()
  }
}
