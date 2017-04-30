package io.neons.testcase

import io.neons.collector.application.config._

trait CollectorConfigTestCase {
  val collectorConfig = CollectorConfig(
    baseConfig = BaseConfig(
      host = "localhost",
      port = 80,
      cookieName = "cookieName",
      cookieDomain = "localhost"
    ),
    trackerConfig = TrackerConfig(
      collectorPath = "/",
      javascriptTrackerFile = "index.min.js"
    ),
    sink = SinkConfig(
      kafkaSinkConfig = KafkaSinkConfig(
        host = "localhost",
        port = 9092,
        topic = "neons",
        clientId = "test"
      )
    )
  )
}
