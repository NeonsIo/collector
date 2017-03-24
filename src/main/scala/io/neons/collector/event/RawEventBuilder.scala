package io.neons.collector.event

import akka.http.scaladsl.model.HttpRequest
import java.time._
import scala.collection.mutable.ListBuffer

case class RawHeaderBag(name: String, value: String)

case class RawEvent(
                     requestUuidL: String,
                     method: String,
                     uri: String,
                     headers: ListBuffer[RawHeaderBag],
                     cookies: ListBuffer[RawHeaderBag],
                     clientIp: String,
                     serverDate: String
                   )

object RawEventBuilder {
  var httpRequest: HttpRequest = _
  var clientIp: String = _
  def applyHttpRequest(httpRequest: HttpRequest) = this.httpRequest = httpRequest
  def applyClientIp(clientIp: String) = this.clientIp = clientIp

  def build: RawEvent = {
    val headersList = new ListBuffer[RawHeaderBag]()
    val cookiesList = new ListBuffer[RawHeaderBag]()
    val zonedDateTime = ZonedDateTime.now.withZoneSameInstant(ZoneId.of("UTC")).toString

    this.httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .filterNot(p => p.lowercaseName() == "cookie")
      .foreach(f => headersList += new RawHeaderBag(f.name, f.value))

    this.httpRequest
      .cookies
      .foreach(f => cookiesList += new RawHeaderBag(f.name, f.value))

    new RawEvent(
      java.util.UUID.randomUUID.toString,
      this.httpRequest.method.toString,
      this.httpRequest.uri.toString,
      headersList,
      cookiesList,
      this.clientIp,
      zonedDateTime.toString
    )
  }
}
