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
  val portBinding = "0.0.0.0"
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

  implicit def exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case ex: Exception => ctx => {
        ctx.log.error(ex, "Uri: " + ctx.request.uri.toString() + "Headers: " + ctx.request.headers)
        ctx.complete(HttpResponse(StatusCodes.InternalServerError, entity = "Internal server error"))
      }
    }

  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(
      injector.getInstance(classOf[Router]).retrieve,
      portBinding,
      config.applicationConfig.port
    )

    actorSystem.log.info(s"Start application with config $config")

    scala.sys.addShutdownHook {
      actorSystem.log.info("Terminating...")
      bindingFuture
        .flatMap(_.unbind())
        .onComplete { _ =>
          materializer.shutdown()
          actorSystem.terminate()
        }
      Await.result(actorSystem.whenTerminated, 60.seconds)
      actorSystem.log.info("Terminated... Bye")
    }
  }
}
