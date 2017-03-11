package io.neons.collector.sink

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.{Paths, StandardOpenOption}
import io.neons.collector.{ReceiveEvent, SendEvent}
import akka.actor.{Actor, Props}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object FilesystemSink {
  def props(location: String): Props = Props(new FilesystemSink(location))
}

class FilesystemSink(location: String) extends Actor {
  implicit val akkaSystem = context.system
  implicit val formats = Serialization.formats(NoTypeHints)

  override def receive: Receive = {
    case SendEvent(event) =>
      val result = Future {
        val path = Paths.get(location + "/" + event.requestUuidL.toString + ".json")
        val asyncFile = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE)
        asyncFile.write(ByteBuffer.wrap(write(event).getBytes()), 0)
      }
      Await.result(result, 1 seconds)
      sender ! ReceiveEvent
  }
}
