package io.neons.collector.infrastructure.log

import java.util.UUID
import akka.http.scaladsl.model.MediaTypes.`image/gif`
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model._
import io.neons.collector.application.directive.TransparentPixel
import io.neons.testcase.CollectorConfigTestCase
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mockito.MockitoSugar

class LogBuilderImplSpec extends FlatSpec with Matchers with MockitoSugar with CollectorConfigTestCase {
  "Log builder" should "build log from http request and client ip" in {
    val builder = new LogBuilderImpl()
    builder.addClientIp("91.100.100.1")
    builder.addHttpRequest(HttpRequest(
      HttpMethods.GET,
      uri = "/collect",
      entity = HttpEntity(`image/gif`, TransparentPixel.pixel)
    )
      .withHeaders(
        RawHeader("timeout-access", "test"),
        RawHeader("cookie", "test"),
        RawHeader("host", "localhost")
      ))
    val result = builder.build

    result.clientIp should be ("91.100.100.1")
    result.headers.length should be (1)
    result.cookies.length should be (0)
    UUID.fromString(result.requestUuidL).toString should be (result.requestUuidL)
    result.method should be ("GET")
    result.uri should be ("/collect")
  }
}
