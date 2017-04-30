package io.neons.collector.infrastructure.log.builder

import akka.http.scaladsl.model.HttpRequest
import io.neons.collector.model.log.{Log, LogBuilder, LogHeaderBag}
import scala.collection.mutable.ListBuffer

class AkkaHttpLogBuilder extends LogBuilder {
  var httpRequest: HttpRequest = _
  var clientIp: String = _

  def addHttpRequest(httpRequest: HttpRequest) = this.httpRequest = httpRequest
  def addClientIp(clientIp: String) = this.clientIp = clientIp

  def build: Log = {
    val headersList = new ListBuffer[LogHeaderBag]()
    val cookiesList = new ListBuffer[LogHeaderBag]()
    val zonedDateTime = System.currentTimeMillis()

    this.httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .filterNot(p => p.lowercaseName() == "cookie")
      .foreach(f => headersList += LogHeaderBag(f.name, f.value))

    httpRequest
      .cookies
      .foreach(f => cookiesList += LogHeaderBag(f.name, f.value))

    Log(
      java.util.UUID.randomUUID.toString,
      httpRequest.method.value,
      httpRequest.uri.toString,
      headersList,
      cookiesList,
      this.clientIp,
      zonedDateTime
    )
  }
}
