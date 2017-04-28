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
  def build(httpRequest: HttpRequest, clientIp: String): Log = {
    val headersList = new ListBuffer[HeaderBag]()
    val cookiesList = new ListBuffer[HeaderBag]()
    val zonedDateTime = System.currentTimeMillis()

    httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .filterNot(p => p.lowercaseName() == "cookie")
      .foreach(f => headersList += HeaderBag(f.name, f.value))

    httpRequest
      .cookies
      .foreach(f => cookiesList += HeaderBag(f.name, f.value))

    Log(
      java.util.UUID.randomUUID.toString,
      httpRequest.method.value,
      httpRequest.uri.toString,
      headersList,
      cookiesList,
      clientIp,
      zonedDateTime
    )
  }
}
