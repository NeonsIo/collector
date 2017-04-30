package io.neons.collector.application.directive

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import io.neons.collector.application.directive.CollectorDirectives._

class CollectorDirectivesSpec extends WordSpec with Matchers with ScalatestRouteTest {
  "The directives for collector" should {
    "return transparent pixel" in {
      Get("/collect") ~> responseWithTransparentPixel ~> check {
        responseAs.status.intValue should be (200)
        responseAs.entity.contentType.mediaType.isImage should be (true)
      }
    }
    "return javascript tracker source with headers" in {
      Get("/index.js") ~> responseWithJavascriptTrackerSource("index.js") ~> check {
        responseAs.status.intValue should be (200)
        responseAs.entity.contentType.mediaType.toString() should be ("application/javascript")
        responseAs.headers.filter(p => p.name() == "Vary").foreach(f => f.value() should be ("Accept-Encoding"))
        responseAs.headers.filter(p => p.name() == "Content-Encoding").foreach(f => f.value() should be ("gzip"))
      }
    }
  }
}
