package io.neons.collector.application.sink

import javax.inject.Inject

import akka.actor.{Actor, ActorRef}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import com.google.inject.name.Named
import io.neons.collector.application.guice.akka.NamedActor
import io.neons.collector.model.log.Log
import io.neons.collector.application.sink.SinkActor.SendLog

object SinkActor extends NamedActor {
  override final val name = "SinkActor"
  case class SendLog(log: Log)
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
    case SendLog(log) => router.route(SendLog(log), sender())
  }
}
