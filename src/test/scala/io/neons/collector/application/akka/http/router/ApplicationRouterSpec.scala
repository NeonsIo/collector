package io.neons.collector.application.akka.http.router

import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.neons.collector.infrastructure.log.builder.AkkaHttpLogBuilder
import io.neons.collector.model.log.{Log, LogHeaderBag, LogSink}
import io.neons.collector.testcase.CollectorConfigTestCase
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar

import scala.collection.mutable.ListBuffer
import org.mockito.Mockito._

import scala.concurrent.Future

class ApplicationRouterSpec extends MockitoSugar with WordSpecLike with Matchers with CollectorConfigTestCase with ScalatestRouteTest {

  "Application router" should {
    "handle basic routes available in application" in {
      val logSink = mock[LogSink]

      val log = Log(
        "3ec0415c-401a-4936-83a0-f21723d0f38a",
        "GET",
        "http://neons.io",
        new ListBuffer[LogHeaderBag],
        new ListBuffer[LogHeaderBag],
        "127.0.0.1",
        1493239462820L
      )
      val logBuilder = mock[AkkaHttpLogBuilder]
      when(logBuilder.build).thenReturn(log)
      when(logSink.sendToSink(log)).thenReturn(Future.successful("test"))
      val router = new ApplicationRouter(collectorConfig, logBuilder, logSink).retrieve

      Get("/index.js") ~> router ~> check {
        responseAs.status.intValue should be (200)
        responseAs.entity.contentType.mediaType.toString() should be ("application/javascript")
        responseAs.headers.filter(p => p.name() == "Vary").foreach(f => f.value() should be ("Accept-Encoding"))
        responseAs.headers.filter(p => p.name() == "Content-Encoding").foreach(f => f.value() should be ("gzip"))
      }

      Get("/collect") ~> router ~> check {
        responseAs.status.intValue should be (200)
        responseAs.entity.contentType.mediaType.isImage should be (true)
      }

      Get("/collect") ~> router ~> check {
        responseAs.status.intValue should be (200)
        responseAs.entity.contentType.mediaType.isImage should be (true)
      }
    }
  }
}
