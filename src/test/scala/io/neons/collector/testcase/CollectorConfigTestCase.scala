package io.neons.collector.testcase

import io.neons.collector.application.config._

trait CollectorConfigTestCase {
  val collectorConfig = CollectorConfig(
    applicationConfig = ApplicationConfig(
      port = 80,
      cookieName = "cookieName",
      cookieDomain = "localhost"
    ),
    trackerConfig = TrackerConfig(
      collectorPath = "collect",
      javascriptTrackerFile = "index.js"
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
