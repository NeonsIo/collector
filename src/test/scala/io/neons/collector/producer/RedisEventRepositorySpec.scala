package io.neons.collector.producer

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class RedisEventRepositorySpec extends WordSpec with Matchers with ScalatestRouteTest {
  "Redis event repository" should {
    "add the raw event from http request" in {
//      new RedisEventRepository()
    }
  }
}
