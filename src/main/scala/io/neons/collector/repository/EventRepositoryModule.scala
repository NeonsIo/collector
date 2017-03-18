package io.neons.collector.repository

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Inject, Provider}
import io.neons.collector.guice.config.CollectorConfig
import io.neons.collector.repository.RedisClientModule.RedisClientProvider
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
