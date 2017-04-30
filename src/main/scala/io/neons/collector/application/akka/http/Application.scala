package io.neons.collector.application.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import io.neons.collector.application.config.CollectorConfig
import io.neons.collector.application.guice.application.config.CollectorConfigModule
import io.neons.collector.application.guice.infrastructure.log.builder.LogBuilderModule
import io.neons.collector.application.guice.application.akka.http.router.RouterModule
import io.neons.collector.application.akka.http.router.Router
import io.neons.collector.application.guice.application.akka.actor.{ActorMaterializerModule, AkkaModule, SinkActorModule, SinkRouterActorModule}
import io.neons.collector.application.guice.infrastructure.log.sink.LogSinkModule

import scala.io.StdIn

object Application {
  val injector = Guice.createInjector(
    new AkkaModule(),
    new ActorMaterializerModule(),
    new CollectorConfigModule(),
    new SinkActorModule(),
    new LogSinkModule(),
    new SinkRouterActorModule(),
    new RouterModule(),
    new LogBuilderModule()
  )

  val config = injector.getInstance(classOf[CollectorConfig])
  implicit val actorSystem = injector.getInstance(classOf[ActorSystem])
  implicit val materializer = injector.getInstance(classOf[ActorMaterializer])
  implicit val executionContext = actorSystem.dispatcher

  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(
      injector.getInstance(classOf[Router]).get,
      config.applicationConfig.host,
      config.applicationConfig.port
    )

    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
  }
}
