package io.neons.collector

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import io.neons.collector.config.CollectorConfig
import io.neons.collector.guice.akka.{ActorMaterializerModule, AkkaModule}
import io.neons.collector.guice.config.CollectorConfigModule
import io.neons.collector.guice.repository.EventRepositoryModule
import io.neons.collector.guice.router.RouterModule
import io.neons.collector.guice.sink.{ProducerSinkActorModule, SinkActorModule}
import io.neons.collector.router.AkkaRouter

import scala.io.StdIn

object Application {
  val injector = Guice.createInjector(
    new AkkaModule(),
    new ActorMaterializerModule(),
    new CollectorConfigModule(),
    new ProducerSinkActorModule(),
    new EventRepositoryModule(),
    new SinkActorModule(),
    new RouterModule()
  )

  val config = injector.getInstance(classOf[CollectorConfig])
  implicit val actorSystem = injector.getInstance(classOf[ActorSystem])
  implicit val materializer = injector.getInstance(classOf[ActorMaterializer])
  implicit val executionContext = actorSystem.dispatcher

  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(
      injector.getInstance(classOf[AkkaRouter]).get,
      config.baseConfig.host,
      config.baseConfig.port
    )

    StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
  }
}
