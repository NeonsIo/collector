package io.neons.collector.directive

import java.util.Base64
import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import io.neons.collector.event.{RawEventBuilder, RawEvent}

object TransparentPixel {
  val pixel = Base64.getDecoder.decode(
    "R0lGODlhAQABAPAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="
  )
}

trait CollectorDirectives {
  def responseWithTransparentPixel: Route = {
    complete(HttpResponse(entity = HttpEntity(`image/gif`, TransparentPixel.pixel)))
  }

  def responseWithJavascriptTrackerSource(trackerFile: String): Route = {
    val headers = List(
      RawHeader("Vary", "Accept-Encoding")
    )
    respondWithHeaders(headers) {
      encodeResponseWith(Gzip) {
        getFromResource(trackerFile)
      }
    }
  }

  def extractRawRequest: Directive1[RawEvent] = {
    val rawEventBuilder = RawEventBuilder

    extractRequest
      .flatMap(request => {
        rawEventBuilder.applyHttpRequest(request)
        extractClientIP
      })
      .flatMap(ip => {
        rawEventBuilder.applyClientIp(ip.toString)
        provide(rawEventBuilder.build)
      })
  }
}

object CollectorDirectives extends CollectorDirectives