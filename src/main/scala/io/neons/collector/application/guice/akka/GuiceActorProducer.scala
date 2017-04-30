package io.neons.collector.application.guice.akka

import akka.actor.{Actor, IndirectActorProducer}
import com.google.inject.name.Names
import com.google.inject.{Injector, Key}

class GuiceActorProducer(injector: Injector, actorName: String) extends IndirectActorProducer {
  override def produce(): Actor = injector.getBinding(Key.get(classOf[Actor], Names.named(actorName))).getProvider.get()
  override def actorClass = classOf[Actor]
}
