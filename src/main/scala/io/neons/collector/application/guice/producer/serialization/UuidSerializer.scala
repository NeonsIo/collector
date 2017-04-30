package io.neons.collector.application.guice.producer.serialization

import java.nio.charset.Charset
import java.util
import java.util.UUID
import org.apache.kafka.common.serialization.Serializer

class UuidSerializer extends Serializer[UUID] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {

  }

  override def serialize(topic: String, data: UUID): Array[Byte] = {
    data.toString.getBytes(Charset.defaultCharset())
  }

  override def close(): Unit = {

  }
}
