package io.neons.collector.directive

import java.util.{Base64, UUID}

import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.model.{DateTime, HttpEntity, HttpResponse}
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.headers.{HttpCookie, RawHeader}
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import io.neons.collector.log.{Log, LogBuilder}

object TransparentPixel {
  val pixel: Array[Byte] = Base64.getDecoder.decode(
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

  def extractRawRequest: Directive1[Log] = {
    val logBuilder = LogBuilder

    extractRequest
      .flatMap(request => {
        logBuilder.applyHttpRequest(request)
        extractClientIP
      })
      .flatMap(ip => {
        logBuilder.applyClientIp(ip.toOption.map(_.getHostAddress).getOrElse("unknown"))
        provide(logBuilder.build)
      })
  }

  def setVisitorCookieAndResponse(name: String, domain: String): Route = {
    optionalCookie(name) {
      case Some(nameCookie) => setCookie(getHttpCookie(name, nameCookie.value, domain)) {
        responseWithTransparentPixel
      }
      case None => setCookie(getHttpCookie(name, UUID.randomUUID().toString, domain)) {
        responseWithTransparentPixel
      }
    }
  }

  def getHttpCookie(name: String, value: String, domain: String): HttpCookie = {
    HttpCookie(name,
      value = value,
      domain = Some(domain),
      expires = Some(DateTime(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30 * 365))),
      path = Some("/")
    )
  }
}

object CollectorDirectives extends CollectorDirectives