package io.neons.collector.router

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Inject
import com.google.inject.name.Named
import io.neons.collector.config.CollectorConfig
import io.neons.collector.sink.SinkActor
import io.neons.collector.sink.SinkActor.SendLog
import io.neons.collector.directive.CollectorDirectives._
import scala.concurrent.duration._

class CollectorRouter @Inject()(collectorConfig: CollectorConfig, @Named(SinkActor.name) sinkActor: ActorRef) extends Router {
  implicit val timeout = Timeout(5.seconds)

  def get: Route = path(collectorConfig.trackerConfig.collectorPath) {
    extractRawRequest { log =>
        onSuccess(sinkActor ? SendLog(log)) { _ =>
          responseWithCookieVisitorId(collectorConfig.baseConfig.cookieName, collectorConfig.baseConfig.cookieDomain)
        }
      }
  } ~
    path(collectorConfig.trackerConfig.javascriptTrackerFile) {
      responseWithJavascriptTrackerSource(collectorConfig.trackerConfig.javascriptTrackerFile)
    }
}
