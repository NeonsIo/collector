package io.neons.collector.event

import akka.http.scaladsl.model.HttpRequest
import java.time._
import scala.collection.mutable.ListBuffer

case class HeaderBag(name: String, value: String)

case class Event(requestUuidL: String,
                 method: String,
                 uri: String,
                 headers: ListBuffer[HeaderBag],
                 cookies: ListBuffer[HeaderBag],
                 clientIp: String,
                 serverDate: String)

object EventBuilder {
  var httpRequest: HttpRequest = _
  var clientIp: String = _
  def applyHttpRequest(httpRequest: HttpRequest) = this.httpRequest = httpRequest
  def applyClientIp(clientIp: String) = this.clientIp = clientIp

  def build: Event = {
    val headersList = new ListBuffer[HeaderBag]()
    val cookiesList = new ListBuffer[HeaderBag]()
    val zonedDateTime = ZonedDateTime.now.withZoneSameInstant(ZoneId.of("UTC")).toString

    this.httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .filterNot(p => p.lowercaseName() == "cookie")
      .foreach(f => headersList += new HeaderBag(f.name, f.value))

    this.httpRequest
      .cookies
      .foreach(f => cookiesList += new HeaderBag(f.name, f.value))

    new Event(
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
