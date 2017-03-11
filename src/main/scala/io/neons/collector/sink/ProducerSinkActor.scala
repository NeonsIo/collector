package io.neons.collector.sink

import akka.actor.Actor
import com.google.inject.Inject
import io.neons.collector.guice.akka.NamedActor
import io.neons.collector.repository.EventRepository
import io.neons.collector.sink.SinkActor.{ReceiveEvent, SendEvent}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ProducerSinkActor extends NamedActor {
  override final val name = "ProducerSinkActor"
}

class ProducerSinkActor @Inject()(eventRepository: EventRepository) extends Actor {
  implicit val akkaSystem = context.system
  implicit val formats = Serialization.formats(NoTypeHints)

  override def receive: Receive = {
    case SendEvent(event) =>
      val result = Future {
        eventRepository.add(event.requestUuidL, write(event))
      }
      Await.result(result, 1.seconds)
      sender ! ReceiveEvent
  }
}
