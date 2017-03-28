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
import io.neons.collector.sink.SinkActor.SendEvent
import io.neons.collector.directive.CollectorDirectives._
import scala.concurrent.duration._

class Router @Inject()(collectorConfig: CollectorConfig, @Named(SinkActor.name) sinkActor: ActorRef) extends AkkaRouter {
  implicit val timeout = Timeout(5.seconds)

  def get: Route = path(collectorConfig.trackerConfig.collectorPath) {
    extractRawRequest { event =>
        onSuccess(sinkActor ? SendEvent(event)) { i =>
          responseWithTransparentPixel
        }
      }
  } ~
    path(collectorConfig.trackerConfig.javascriptTrackerFile) {
      responseWithJavascriptTrackerSource(collectorConfig.trackerConfig.javascriptTrackerFile)
    }
}
