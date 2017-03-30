package io.neons.collector.router

import akka.http.scaladsl.server._

trait Router {
  def get: Route
}
