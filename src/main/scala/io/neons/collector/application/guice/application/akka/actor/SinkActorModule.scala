package io.neons.collector.application.guice.application.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem}
import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Inject, Provides}
import io.neons.collector.application.akka.actor.SinkActor
import net.codingwell.scalaguice.ScalaModule

class SinkActorModule extends AbstractModule with ScalaModule with GuiceAkkaActorRefProvider {
  override def configure(): Unit = {
    bind[Actor].annotatedWith(Names.named(SinkActor.name)).to[SinkActor]
  }

  @Provides
  @Named(SinkActor.name)
  def provideRedisSinkActorRef(@Inject() system: ActorSystem): ActorRef = provideActorRef(system, SinkActor.name)
}
