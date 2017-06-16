package io.neons.collector.application.akka.http

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mockito.MockitoSugar

class ApplicationSpec extends FlatSpec with Matchers with MockitoSugar {
  "Application" should "bootstrap and run server" in {
    Application.main(new Array[String](0))
  }
}
