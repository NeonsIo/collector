package io.neons.collector.sink

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import io.neons.collector.{ReceiveEvent, SendEvent}
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.write
import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class SinkActor extends Actor {
  implicit val akkaSystem = context.system
  val conf = ConfigFactory.load("collector")
  val redis = RedisClient(
    conf.getString("collector.sink.redis.host"),
    conf.getInt("collector.sink.redis.port")
  )

  override def receive: Receive = {
    case SendEvent(event) => {
        implicit val formats = Serialization.formats(NoTypeHints)

        val result = Future {
          redis.set(event.requestUuidL.toString, write(event))
        }
        Await.result(result, 5 seconds)
        sender ! ReceiveEvent
    }
  }
}
