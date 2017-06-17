package io.neons.collector.application.akka.http.router

import akka.http.scaladsl.server._

trait Router {
  def retrieve: Route
}
