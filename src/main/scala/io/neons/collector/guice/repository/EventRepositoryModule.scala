package io.neons.collector.guice.repository

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Inject, Provider}
import io.neons.collector.config.CollectorConfig
import io.neons.collector.guice.repository.RedisClientModule.RedisClientProvider
import io.neons.collector.repository.{EventRepository, RedisEventRepository}
import net.codingwell.scalaguice.ScalaModule
import redis.RedisClient

object RedisClientModule {
  class RedisClientProvider @Inject()(collectorConfig: CollectorConfig, actorSystem: ActorSystem) extends Provider[RedisClient] {
    override def get(): RedisClient = {
      RedisClient(
        host = collectorConfig.sink.redisSinkConfig.host,
        port = collectorConfig.sink.redisSinkConfig.port
      )(
        _system = actorSystem
      )
    }
  }
}

class EventRepositoryModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[EventRepository].to[RedisEventRepository].asEagerSingleton()
    bind[RedisClient].toProvider[RedisClientProvider].asEagerSingleton()
  }
}
