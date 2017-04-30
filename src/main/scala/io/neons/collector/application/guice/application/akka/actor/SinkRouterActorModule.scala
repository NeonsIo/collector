package io.neons.collector.application.guice.application.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem}
import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Inject, Provides}
import io.neons.collector.application.akka.actor.{SinkActor, SinkRouterActor}
import net.codingwell.scalaguice.ScalaModule

class SinkRouterActorModule extends AbstractModule with ScalaModule with GuiceAkkaActorRefProvider {
  override def configure(): Unit = {
    bind[Actor].annotatedWith(Names.named(SinkRouterActor.name)).to[SinkRouterActor]
  }

  @Provides
  @Named(SinkRouterActor.name)
  def provideSinkActorRef(@Inject() system: ActorSystem): ActorRef = provideActorRef(system, SinkActor.name)
}
