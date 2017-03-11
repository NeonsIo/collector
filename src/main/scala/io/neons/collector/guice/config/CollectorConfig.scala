package io.neons.collector.guice.config

import com.typesafe.config.Config

case class BaseConfig(host: String, port: Int)

case class SinkConfig(redisSinkConfig: RedisSinkConfig)

case class RedisSinkConfig(host: String, port: Int)

case class TrackerConfig(collectorPath: String, javascriptTrackerFile: String)

case class CollectorConfig(baseConfig: BaseConfig, trackerConfig: TrackerConfig, sink: SinkConfig)

object CollectorConfig {
  def load(config: Config) = {
    CollectorConfig(
      baseConfig = BaseConfig(
        host = config.getString("collector.base.host"),
        port = config.getInt("collector.base.port")
      ),
      trackerConfig = TrackerConfig(
        collectorPath = config.getString("collector.tracker.collector-path"),
        javascriptTrackerFile = config.getString("collector.tracker.javascript-tracker-file")
      ),
      sink = SinkConfig(
        redisSinkConfig = RedisSinkConfig(
          host = config.getString("collector.sink.redis.host"),
          port = config.getInt("collector.sink.redis.port")
        )
      )
    )
  }
}
