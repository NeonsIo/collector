package io.neons.collector.application.guice.application.akka.actor

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.google.inject.{AbstractModule, Provider}
import io.neons.collector.application.guice.application.akka.actor.ActorMaterializerModule.ActorMaterializerProvider
import net.codingwell.scalaguice.ScalaModule

object ActorMaterializerModule {
  class ActorMaterializerProvider @Inject()(actorSystem: ActorSystem) extends Provider[ActorMaterializer] {
    override def get(): ActorMaterializer = ActorMaterializer.apply()(context = actorSystem)
  }
}

class ActorMaterializerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActorMaterializer].toProvider[ActorMaterializerProvider].asEagerSingleton()
  }
}
