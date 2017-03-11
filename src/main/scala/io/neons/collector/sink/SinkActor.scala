package io.neons.collector.sink

import akka.actor.{Actor, Props}
import io.neons.collector.SendEvent
import redis.RedisClient
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

object SinkActor {
  def props(redisClient: RedisClient): Props = Props(new SinkActor(redisClient))
}

class SinkActor(redisClient: RedisClient) extends Actor {
  implicit val akkaSystem = context.system

  val router = {
    val routees = Vector.fill(8) {
      val sinkActor = akkaSystem.actorOf(RedisSinkActor.props(redisClient))
      context watch sinkActor
      ActorRefRoutee(sinkActor)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case SendEvent(event) => router.route(SendEvent(event), sender())
  }
}
