package io.neons.collector.application.akka.actor

import javax.inject.Inject

import akka.actor.{Actor, ActorRef}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import com.google.inject.name.Named
import io.neons.collector.application.akka.actor.SinkRouterActor.SendLog
import io.neons.collector.application.guice.application.akka.actor.NamedActor
import io.neons.collector.model.log.Log

object SinkRouterActor extends NamedActor {
  override final val name = "SinkRouterActor"
  case class SendLog(log: Log)
  case object ReceiveEvent
}

class SinkRouterActor @Inject()(@Named(SinkActor.name) sinkActor: ActorRef) extends Actor {
  implicit val akkaSystem = context.system

  val router = {
    val routees = Vector.fill(1) {
      context watch sinkActor
      ActorRefRoutee(sinkActor)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case SendLog(log) => router.route(SendLog(log), sender())
  }
}
