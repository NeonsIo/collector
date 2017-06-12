package io.neons.collector.application.akka.http.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.google.inject.Inject
import io.github.lhotari.akka.http.health.HealthEndpoint
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.application.akka.http.directive.CollectorDirectives._
import io.neons.collector.model.log.{LogBuilder, LogSink}
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationRouter @Inject()(collectorConfig: CollectorConfig, logBuilder: LogBuilder, logSink: LogSink) extends Router {
  def get: Route = path(collectorConfig.trackerConfig.collectorPath) {
    extractRawRequest(logBuilder) { log =>
      onSuccess(logSink.sendToSink(log)) { _ =>
        responseWithCookieVisitorId(collectorConfig.applicationConfig.cookieName, collectorConfig.applicationConfig.cookieDomain)
      }
    }
  } ~
    path(collectorConfig.trackerConfig.javascriptTrackerFile) {
      responseWithJavascriptTrackerSource(collectorConfig.trackerConfig.javascriptTrackerFile)
    } ~
    HealthEndpoint.createDefaultHealthRoute()
}
