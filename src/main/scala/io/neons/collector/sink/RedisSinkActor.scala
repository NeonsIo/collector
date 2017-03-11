package io.neons.collector.sink

import io.neons.collector.{ReceiveEvent, SendEvent}
import akka.actor.{Actor, Props}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import redis.RedisClient

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object RedisSinkActor {
  def props(redisClient: RedisClient): Props = Props(new RedisSinkActor(redisClient))
}

class RedisSinkActor(redisClient: RedisClient) extends Actor {
  implicit val akkaSystem = context.system
  implicit val formats = Serialization.formats(NoTypeHints)

  override def receive: Receive = {
    case SendEvent(event) => {
      val result = Future {
        redisClient.set(event.requestUuidL.toString, write(event))
      }
      Await.result(result, 1 seconds)
      sender ! ReceiveEvent
    }
  }
}
