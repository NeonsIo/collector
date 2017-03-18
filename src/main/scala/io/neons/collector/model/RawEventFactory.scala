package io.neons.collector.model

import akka.http.scaladsl.model.HttpRequest

import scala.collection.mutable.ListBuffer

case class RawHeaderBag(name: String, value: String)

case class RawEvent(requestUuidL: String, method: String, uri: String, headers: ListBuffer[RawHeaderBag], clientIp: String)

class RawEventFactory(httpRequest: HttpRequest, clientIp: String) {
  def create: RawEvent = {
    val headersList = new ListBuffer[RawHeaderBag]()

    httpRequest
      .headers
      .filterNot(p => p.lowercaseName == "timeout-access")
      .foreach(f => headersList += new RawHeaderBag(f.name(), f.value()))

    new RawEvent(
      java.util.UUID.randomUUID.toString,
      httpRequest.method.toString,
      httpRequest.uri.toString,
      headersList,
      clientIp)
  }
}
