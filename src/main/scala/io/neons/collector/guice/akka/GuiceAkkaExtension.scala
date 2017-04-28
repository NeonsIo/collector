package io.neons.collector.guice.akka

import akka.actor._
import com.google.inject.Injector

class GuiceAkkaExtensionImpl extends Extension {
  private var injector: Injector = _

  def initialize(injector: Injector) = {
    this.injector = injector
  }

  def props(actorName: String) = Props(classOf[GuiceActorProducer], injector, actorName)
}

object GuiceAkkaExtension extends ExtensionId[GuiceAkkaExtensionImpl] with ExtensionIdProvider {
  override def createExtension(system: ExtendedActorSystem) = new GuiceAkkaExtensionImpl
  override def lookup() = GuiceAkkaExtension
  override def get(system: ActorSystem): GuiceAkkaExtensionImpl = super.get(system)
}

trait NamedActor {
  def name: String
}

trait GuiceAkkaActorRefProvider {
  def propsFor(system: ActorSystem, name: String) = GuiceAkkaExtension(system).props(name)
  def provideActorRef(system: ActorSystem, name: String): ActorRef = system.actorOf(propsFor(system, name))
}
