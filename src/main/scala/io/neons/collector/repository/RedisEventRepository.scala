package io.neons.collector.repository

import akka.actor.ActorSystem
import com.google.inject.Inject
import redis.RedisClient

import scala.concurrent.Future

class RedisEventRepository @Inject()(redisClient: RedisClient, implicit val actorSystem: ActorSystem) extends EventRepository {
  def add(uuid: String, value: String): Future[Boolean] = {
    redisClient.set(uuid.toString, value)
  }
}

