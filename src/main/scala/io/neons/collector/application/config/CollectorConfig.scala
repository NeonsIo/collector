package io.neons.collector.application.config

import com.typesafe.config.Config

case class BaseConfig(host: String, port: Int, cookieName: String, cookieDomain: String)

case class SinkConfig(kafkaSinkConfig: KafkaSinkConfig)

case class KafkaSinkConfig(host: String, port: Int, topic: String, clientId: String)

case class TrackerConfig(collectorPath: String, javascriptTrackerFile: String)

case class CollectorConfig(baseConfig: BaseConfig, trackerConfig: TrackerConfig, sink: SinkConfig)

object CollectorConfig {
  def load(config: Config) = {
    CollectorConfig(
      baseConfig = BaseConfig(
        host = config.getString("collector.base.host"),
        port = config.getInt("collector.base.port"),
        cookieName = config.getString("collector.base.cookie-name"),
        cookieDomain = config.getString("collector.base.cookie-domain")
      ),
      trackerConfig = TrackerConfig(
        collectorPath = config.getString("collector.tracker.collector-path"),
        javascriptTrackerFile = config.getString("collector.tracker.javascript-tracker-file")
      ),
      sink = SinkConfig(
        kafkaSinkConfig = KafkaSinkConfig(
          host = config.getString("collector.sink.kafka.host"),
          port = config.getInt("collector.sink.kafka.port"),
          topic = config.getString("collector.sink.kafka.topic"),
          clientId = config.getString("collector.sink.kafka.client-id")
        )
      )
    )
  }
}
