package io.neons.collector.application.guice.application.akka.actor

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Injector, Provider}
import io.neons.collector.application.guice.application.akka.actor.AkkaModule.ActorSystemProvider
import net.codingwell.scalaguice.ScalaModule

object AkkaModule {
  class ActorSystemProvider @Inject()(injector: Injector) extends Provider[ActorSystem] {
    override def get = {
      val actorSystem = ActorSystem("collector")
      GuiceAkkaExtension(actorSystem).initialize(injector)
      actorSystem
    }
  }
}

class AkkaModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActorSystem].toProvider[ActorSystemProvider].asEagerSingleton()
  }
}
