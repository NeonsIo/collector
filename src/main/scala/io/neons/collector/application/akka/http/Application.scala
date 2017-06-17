package io.neons.collector.application.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.application.guice.application.config.CollectorConfigModule
import io.neons.collector.application.guice.infrastructure.log.builder.LogBuilderModule
import io.neons.collector.application.guice.application.akka.http.router.RouterModule
import io.neons.collector.application.akka.http.router.Router
import io.neons.collector.application.guice.application.akka.actor._
import io.neons.collector.application.guice.infrastructure.log.sink.LogSinkModule
import scala.concurrent.Await
import scala.concurrent.duration._

object Application {
  val injector = Guice.createInjector(
    new AkkaModule(),
    new ActorMaterializerModule(),
    new CollectorConfigModule(),
    new LogSinkModule(),
    new RouterModule(),
    new LogBuilderModule()
  )

  val config = injector.getInstance(classOf[CollectorConfig])
  implicit val actorSystem = injector.getInstance(classOf[ActorSystem])
  implicit val materializer = injector.getInstance(classOf[ActorMaterializer])
  implicit val executionContext = actorSystem.dispatcher

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ex: Exception => ctx => {
        ctx.log.error(ex, "Uri: " + ctx.request.uri.toString())
        ctx.complete(HttpResponse(StatusCodes.InternalServerError, entity = "Internal server error"))
      }
    }

  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(
      injector.getInstance(classOf[Router]).retrieve,
      "0.0.0.0",
      config.applicationConfig.port
    )

    scala.sys.addShutdownHook {
      println("Terminating...")
      bindingFuture
        .flatMap(_.unbind())
        .onComplete { _ =>
          materializer.shutdown()
          actorSystem.terminate()
        }
      Await.result(actorSystem.whenTerminated, 60.seconds)
      println("Terminated... Bye")
    }
  }
}
