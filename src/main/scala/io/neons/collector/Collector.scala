package io.neons.collector

import akka.pattern.ask
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import scala.io.StdIn
import akka.util.Timeout
import io.neons.collector.model.{RawEvent, RawEventFactory}
import io.neons.collector.sink.SinkActor
import scala.concurrent.duration._

case class SendEvent(event: RawEvent)
case object ReceiveEvent

object Collector {
  def main(args: Array[String]) {
    val conf = ConfigFactory.load("collector")
    implicit val system = ActorSystem("collector")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val timeout = Timeout(5 seconds)

    val router = system.actorOf(Props(new SinkActor()))

    val route: Route = get {
      extractRequest { request =>
        extractClientIP { ip =>
          val event = new RawEventFactory(request, ip.toString).create
          onSuccess(router ? SendEvent(event)) { i =>
            complete("")
          }
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(
      route, conf.getString("collector.base.host"), conf.getInt("collector.base.port")
    )

    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}


