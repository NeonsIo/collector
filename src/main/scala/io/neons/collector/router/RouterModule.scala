package io.neons.collector.router

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class RouterModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[AkkaRouter].to[Router].asEagerSingleton()
  }
}
