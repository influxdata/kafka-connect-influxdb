package com.influxdata.influxdb

import java.util
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.connect.sink.{SinkRecord, SinkTask}
import scala.collection.JavaConverters._
import scala.util.{Success, Failure}
import com.typesafe.scalalogging.LazyLogging

class InfluxDBTask extends SinkTask with LazyLogging {
  var writer: Option[InfluxDBWriter] = None

  override def start(props: util.Map[String, String]): Unit = {
    val sinkConfig = new InfluxDBSinkConfig(props)
    writer = Some(
      new InfluxDBWriter()
    )
  }

  override def put(records: util.Collection[SinkRecord]): Unit =
    records.asScala
      .map(_.value.toString)
      .map(text =>
        (text, writer match {
          case Some(writer) => writer.writePoint(text)
          case None =>
            Failure(new IllegalStateException("InfluxDB writer is not set"))
        })
      )
      .foreach {
        case (text, result) =>
          result match {
            case Success(id) =>
              logger.info(
                s"successfully tweeted `${text}`; got assigned id ${id}"
              )
            case Failure(err) =>
              logger.warn(s"tweeting `${text}` failed: ${err.getMessage}")
          }
      }

  override def stop(): Unit = {}

  override def flush(map: util.Map[TopicPartition, OffsetAndMetadata]) = {}
  override def version(): String = ""
}
