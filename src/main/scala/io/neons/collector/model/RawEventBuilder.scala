package io.neons.collector.model

import akka.http.scaladsl.model.HttpRequest

import scala.collection.mutable.ListBuffer

case class RawHeaderBag(name: String, value: String)

case class RawEvent(requestUuidL: String, method: String, uri: String, headers: ListBuffer[RawHeaderBag], clientIp: String)

object RawEventBuilder {
  var httpRequest: HttpRequest = _
  var clientIp: String = _
  def applyHttpRequest(httpRequest: HttpRequest) = this.httpRequest = httpRequest
  def applyClientIp(clientIp: String) = this.clientIp = clientIp

  def build: RawEvent = {
    val headersList = new ListBuffer[RawHeaderBag]()

    this.httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .foreach(f => headersList += new RawHeaderBag(f.name(), f.value()))

    new RawEvent(
      java.util.UUID.randomUUID.toString,
      this.httpRequest.method.toString,
      this.httpRequest.uri.toString,
      headersList,
      this.clientIp)
  }
}
