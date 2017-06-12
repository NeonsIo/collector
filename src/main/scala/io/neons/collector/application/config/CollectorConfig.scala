package io.neons.collector.application.config

import com.typesafe.config.Config

case class ApplicationConfig(port: Int, cookieName: String, cookieDomain: String)

case class SinkConfig(kafkaSinkConfig: KafkaSinkConfig)

case class KafkaSinkConfig(host: String, port: Int, topic: String, clientId: String)

case class TrackerConfig(collectorPath: String, javascriptTrackerFile: String)

case class CollectorConfig(applicationConfig: ApplicationConfig, trackerConfig: TrackerConfig, sink: SinkConfig)

object CollectorConfig {
  def load(config: Config) = {
    CollectorConfig(
      applicationConfig = ApplicationConfig(
        port = config.getInt("collector.application.port"),
        cookieName = config.getString("collector.application.cookie-name"),
        cookieDomain = config.getString("collector.application.cookie-domain")
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
