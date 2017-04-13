package io.neons.collector.log

import akka.http.scaladsl.model.HttpRequest

import scala.collection.mutable.ListBuffer

case class HeaderBag(name: String, value: String)

case class Log(requestUuidL: String,
               method: String,
               uri: String,
               headers: ListBuffer[HeaderBag],
               cookies: ListBuffer[HeaderBag],
               clientIp: String,
               serverDate: Long)

object LogBuilder {
  var httpRequest: HttpRequest = _
  var clientIp: String = _
  def applyHttpRequest(httpRequest: HttpRequest) = this.httpRequest = httpRequest
  def applyClientIp(clientIp: String) = this.clientIp = clientIp

  def build: Log = {
    val headersList = new ListBuffer[HeaderBag]()
    val cookiesList = new ListBuffer[HeaderBag]()
    val zonedDateTime = System.currentTimeMillis()

    this.httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .filterNot(p => p.lowercaseName() == "cookie")
      .foreach(f => headersList += new HeaderBag(f.name, f.value))

    this.httpRequest
      .cookies
      .foreach(f => cookiesList += new HeaderBag(f.name, f.value))

    new Log(
      java.util.UUID.randomUUID.toString,
      this.httpRequest.method.value,
      this.httpRequest.uri.toString,
      headersList,
      cookiesList,
      this.clientIp,
      zonedDateTime
    )
  }
}
