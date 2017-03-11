package io.neons.collector.sink

import javax.inject.Inject

import akka.actor.{Actor, ActorRef}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import com.google.inject.name.Named
import io.neons.collector.guice.akka.NamedActor
import io.neons.collector.model.RawEvent
import io.neons.collector.sink.SinkActor.SendEvent

object SinkActor extends NamedActor {
  override final val name = "SinkActor"
  case class SendEvent(event: RawEvent)
  case object ReceiveEvent
}

class SinkActor @Inject()(@Named(ProducerSinkActor.name) producerSinkActor: ActorRef) extends Actor {
  implicit val akkaSystem = context.system

  val router = {
    val routees = Vector.fill(1) {
      context watch producerSinkActor
      ActorRefRoutee(producerSinkActor)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case SendEvent(event) => router.route(SendEvent(event), sender())
  }
}
