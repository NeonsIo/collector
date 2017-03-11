package io.neons.collector.router

import akka.http.scaladsl.server._

trait AkkaRouter {
  def get: Route
}
