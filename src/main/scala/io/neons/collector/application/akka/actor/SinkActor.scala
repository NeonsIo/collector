package io.neons.collector.application.akka.actor

import akka.actor.Actor
import com.google.inject.Inject
import io.neons.collector.application.akka.actor.SinkRouterActor.{ReceiveEvent, SendLog}
import io.neons.collector.application.guice.application.akka.actor.NamedActor
import io.neons.collector.model.log.LogSink
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object SinkActor extends NamedActor {
  override final val name = "SinkActor"
}

class SinkActor @Inject()(logSink: LogSink) extends Actor {
  implicit val akkaSystem = context.system

  override def receive: Receive = {
    case SendLog(log) =>
      val result = Future {
        logSink.sendToSink(log)
      }
      Await.result(result, 5.seconds)
      sender ! ReceiveEvent
  }
}
