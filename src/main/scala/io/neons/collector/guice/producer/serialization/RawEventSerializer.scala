package io.neons.collector.guice.producer.serialization

import java.nio.charset.Charset
import java.util

import io.neons.collector.event.Event
import org.apache.kafka.common.serialization.Serializer
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

class RawEventSerializer extends Serializer[Event] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {

  }

  override def serialize(topic: String, data: Event): Array[Byte] = {
    implicit val formats = Serialization.formats(NoTypeHints)
    write(data).getBytes(Charset.defaultCharset())
  }

  override def close(): Unit = {

  }
}
