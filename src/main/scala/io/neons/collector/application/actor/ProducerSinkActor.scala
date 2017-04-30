package io.neons.collector.application.sink

import akka.actor.Actor
import com.google.inject.Inject
import io.neons.collector.application.guice.akka.NamedActor
import io.neons.collector.model.log.LogProducer
import io.neons.collector.application.sink.SinkActor.{ReceiveEvent, SendLog}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ProducerSinkActor extends NamedActor {
  override final val name = "ProducerSinkActor"
}

class ProducerSinkActor @Inject()(logProducer: LogProducer) extends Actor {
  implicit val akkaSystem = context.system

  override def receive: Receive = {
    case SendLog(log) =>
      val result = Future {
        logProducer.produce(log)
      }
      Await.result(result, 5.seconds)
      sender ! ReceiveEvent
  }
}
