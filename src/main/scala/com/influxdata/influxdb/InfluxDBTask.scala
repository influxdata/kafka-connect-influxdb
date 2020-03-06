package com.influxdata.influxdb

import java.util
import java.util.HashMap
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.connect.sink.{SinkRecord, SinkTask}
import scala.collection.JavaConverters._
import scala.util.{Success, Failure}
import com.typesafe.scalalogging.LazyLogging
import com.influxdb.client.write.Point
import java.time.Instant
import com.influxdb.client.domain.WritePrecision
import org.apache.kafka.connect.data.Struct

class InfluxDBTask extends SinkTask with LazyLogging {
  var writer: Option[InfluxDBWriter] = None
  var sinkConfig: Option[InfluxDBSinkConfig] = None

  override def start(props: util.Map[String, String]): Unit = {
    this.sinkConfig = Some(new InfluxDBSinkConfig(props))

    this.writer = Some(
      new InfluxDBWriter(
        url = sinkConfig.get.getString(InfluxDBSinkConfig.INFLUXDB_URL),
        org = sinkConfig.get.getString(InfluxDBSinkConfig.INFLUXDB_ORG),
        token = sinkConfig.get.getString(InfluxDBSinkConfig.INFLUXDB_TOKEN),
        bucket = sinkConfig.get.getString(InfluxDBSinkConfig.INFLUXDB_BUCKET)
      )
    )
  }

  override def put(records: util.Collection[SinkRecord]): Unit = {
    if (records.size() > 0) {
      records.forEach((record: SinkRecord) => {
        val topic = record.topic()

        var struct: HashMap[String, Any] =
          record.value().asInstanceOf[HashMap[String, Any]]

        // TODO: allow config to specify a field in the record as the timestamp
        val timestamp = record.timestamp()

        this.writer match {
          case Some(writer) => {
            val p = Point
              .measurement(
                this.sinkConfig.get
                  .getString(InfluxDBSinkConfig.INFLUXDB_MEASUREMENT)
              )
              .time(timestamp, WritePrecision.MS)

            struct.forEach((k, v) => {
              v match {
                case s: String            => p.addTag(k, s)
                case l: java.lang.Long    => p.addField(k, l)
                case d: java.lang.Double  => p.addField(k, d)
                case i: java.lang.Integer => p.addField(k, i)
                case b: java.lang.Boolean => p.addField(k, b)
              }
            })

            writer.writePoint(p)
          }

          case None =>
        }
      })
    }
  }

  override def stop(): Unit = {}

  override def flush(map: util.Map[TopicPartition, OffsetAndMetadata]) = {}
  override def version(): String = "1.0.0"
}
