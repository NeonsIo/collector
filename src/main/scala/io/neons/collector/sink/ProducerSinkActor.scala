package io.neons.collector.sink

import akka.actor.Actor
import com.google.inject.Inject
import io.neons.collector.guice.akka.NamedActor
import io.neons.collector.producer.EventProducer
import io.neons.collector.sink.SinkActor.{ReceiveEvent, SendEvent}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ProducerSinkActor extends NamedActor {
  override final val name = "ProducerSinkActor"
}

class ProducerSinkActor @Inject()(eventRepository: EventProducer) extends Actor {
  implicit val akkaSystem = context.system

  override def receive: Receive = {
    case SendEvent(event) =>
      val result = Future {
        eventRepository.produce(event)
      }
      Await.result(result, 5.seconds)
      sender ! ReceiveEvent
  }
}
