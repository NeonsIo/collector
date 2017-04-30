package io.neons.collector.application.router

import akka.http.scaladsl.server._

trait Router {
  def get: Route
}
