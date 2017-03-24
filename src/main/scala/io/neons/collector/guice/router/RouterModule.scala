package io.neons.collector.guice.router

import com.google.inject.AbstractModule
import io.neons.collector.router.{AkkaRouter, Router}
import net.codingwell.scalaguice.ScalaModule

class RouterModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[AkkaRouter].to[Router].asEagerSingleton()
  }
}
