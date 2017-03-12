package io.neons.collector

import akka.pattern.ask
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.io.StdIn
import akka.util.Timeout
import io.neons.collector.model.{RawEvent, RawEventFactory}
import io.neons.collector.sink.SinkActor
import redis.RedisClient

import scala.concurrent.duration._

case class SendEvent(event: RawEvent)
case object ReceiveEvent

object Collector {

  val conf = ConfigFactory.load()
  implicit val system = ActorSystem("collector")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(5 seconds)
  val redis = RedisClient(
    conf.getString("collector.sink.redis.host"),
    conf.getInt("collector.sink.redis.port")
  )

  def main(args: Array[String]) {

    val sink = system.actorOf(SinkActor.props(redis), "sink")

    val route: Route = get {
      extractRequest { request =>
        extractClientIP { ip =>
          val event = new RawEventFactory(request, ip.toString).create
          onSuccess(sink ? SendEvent(event)) { i =>
            complete("")
          }
        }
      }
    }
    val bindingFuture = Http().bindAndHandle(
      route,
      conf.getString("collector.base.host"),
      conf.getInt("collector.base.port")
    )

    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}


