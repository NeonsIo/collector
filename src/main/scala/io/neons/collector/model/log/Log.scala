package io.neons.collector.model.log

import scala.collection.mutable.ListBuffer

case class Log(requestUuidL: String,
               method: String,
               uri: String,
               headers: ListBuffer[LogHeaderBag],
               cookies: ListBuffer[LogHeaderBag],
               clientIp: String,
               serverDate: Long)
