package com.influxdata.influxdb

import java.util
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.connect.sink.{SinkRecord, SinkTask}
import scala.collection.JavaConverters._
import scala.util.{Success, Failure}
import com.typesafe.scalalogging.LazyLogging
import com.influxdb.client.write.Point
import java.time.Instant
import com.influxdb.client.domain.WritePrecision

class InfluxDBTask extends SinkTask with LazyLogging {
  var writer: Option[InfluxDBWriter] = None
  var sinkConfig: Option[InfluxDBSinkConfig] = None

  override def start(props: util.Map[String, String]): Unit = {
    this.sinkConfig = Some(new InfluxDBSinkConfig(props))

    writer = Some(
      new InfluxDBWriter(
        sinkConfig.get.getString(InfluxDBSinkConfig.INFLUXDB_URL),
        sinkConfig.get.getString(InfluxDBSinkConfig.INFLUXDB_TOKEN)
      )
    )
  }

  override def put(records: util.Collection[SinkRecord]): Unit = {
    if (records.size() > 0) {
      records.forEach((record: SinkRecord) => {
        val topic = record.topic()

        // TODO: allow config to specify a field in the record as the timestamp
        val timestamp = record.timestamp()

        this.writer match {
          case Some(writer) => {
            writer.writePoint(
              Point
                .measurement(
                  this.sinkConfig.get
                    .getString(InfluxDBSinkConfig.INFLUXDB_MEASUREMENT)
                )
                .time(timestamp, WritePrecision.MS)
                .addTag("tag", "value")
                .addField("field", 0)
            )
          }
        }
      })
    }
  }

  override def stop(): Unit = {}

  override def flush(map: util.Map[TopicPartition, OffsetAndMetadata]) = {}
  override def version(): String = ""
}
