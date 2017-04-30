package io.neons.collector.application.akka.http.router

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Inject
import com.google.inject.name.Named
import io.neons.collector.application.akka.actor.SinkRouterActor
import io.neons.collector.application.akka.actor.SinkRouterActor.SendLog
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.application.akka.http.directive.CollectorDirectives._
import io.neons.collector.model.log.LogBuilder

import scala.concurrent.duration._

class ApplicationRouter @Inject()(collectorConfig: CollectorConfig, logBuilder: LogBuilder, @Named(SinkRouterActor.name) sinkActor: ActorRef) extends Router {
  implicit val timeout = Timeout(5.seconds)

  def get: Route = path(collectorConfig.trackerConfig.collectorPath) {
    extractRawRequest(logBuilder) { log =>
        onSuccess(sinkActor ? SendLog(log)) { _ =>
          responseWithCookieVisitorId(collectorConfig.applicationConfig.cookieName, collectorConfig.applicationConfig.cookieDomain)
        }
      }
  } ~
    path(collectorConfig.trackerConfig.javascriptTrackerFile) {
      responseWithJavascriptTrackerSource(collectorConfig.trackerConfig.javascriptTrackerFile)
    }
}
