package io.neons.collector.guice.sink

import akka.actor.{Actor, ActorRef, ActorSystem}
import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Inject, Provides}
import io.neons.collector.guice.akka.GuiceAkkaActorRefProvider
import io.neons.collector.sink.{ProducerSinkActor, SinkActor}
import net.codingwell.scalaguice.ScalaModule

class SinkActorModule extends AbstractModule with ScalaModule with GuiceAkkaActorRefProvider {
  override def configure(): Unit = {
    bind[Actor].annotatedWith(Names.named(SinkActor.name)).to[SinkActor]
  }

  @Provides
  @Named(SinkActor.name)
  def provideSinkActorRef(@Inject() system: ActorSystem): ActorRef = provideActorRef(system, ProducerSinkActor.name)
}
