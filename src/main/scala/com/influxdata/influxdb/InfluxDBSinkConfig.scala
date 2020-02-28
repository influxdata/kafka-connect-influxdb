package com.influxdata.influxdb

import java.util

import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}
import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.connect.sink.SinkTask

object InfluxDBSinkConfig {
  val INFLUXDB_URL = "influxdb.url"
  val INFLUXDB_URL_DOC =
    "The URL of your InfluxDB server (with port number and protocol)"

  val INFLUXDB_ORG = "influxdb.org"
  val INFLUXDB_ORG_DOC = "The InfluxDB organization name or ID"

  val INFLUXDB_TOKEN = "influxdb.token"
  val INFLUXDB_TOKEN_DOC = "The InfluxDB token"

  val INFLUXDB_BUCKET = "influxdb.bucket"
  val INFLUXDB_BUCKET_DOC = "The InfluxDB bucket name"

  val INFLUXDB_MEASUREMENT = "influxdb.measurement"
  val INFLUXDB_MEASUREMENT_DOC =
    "The name of the measurement for storing points"

  val INFLUXDB_MEASUREMENT_FIELD = "influxdb.measurement_field"
  val INFLUXDB_MEASUREMENT_FIELD_DOC =
    "The field name in the payload to use as the measurement name"

  val TOPICS = SinkTask.TOPICS_CONFIG
  val TOPICS_DOC = "The Kafka topic to read from."

  val config: ConfigDef = new ConfigDef()
    .define(INFLUXDB_URL, Type.STRING, Importance.HIGH, INFLUXDB_URL_DOC)
    .define(INFLUXDB_ORG, Type.STRING, Importance.HIGH, INFLUXDB_ORG_DOC)
    .define(INFLUXDB_TOKEN, Type.STRING, Importance.HIGH, INFLUXDB_TOKEN_DOC)
    .define(INFLUXDB_BUCKET, Type.STRING, Importance.HIGH, INFLUXDB_BUCKET_DOC)
    .define(
      INFLUXDB_MEASUREMENT,
      Type.STRING,
      "",
      Importance.LOW,
      INFLUXDB_MEASUREMENT_DOC
    )
    .define(
      INFLUXDB_MEASUREMENT_FIELD,
      Type.STRING,
      "",
      Importance.LOW,
      INFLUXDB_MEASUREMENT_FIELD_DOC
    )
    .define(TOPICS, Type.LIST, Importance.HIGH, TOPICS_DOC)
}

class InfluxDBSinkConfig(props: util.Map[String, String])
    extends AbstractConfig(InfluxDBSinkConfig.config, props) {}
