package com.influxdata.influxdb

import scala.util.Try
import com.typesafe.scalalogging.LazyLogging
import com.influxdb.client.scala.InfluxDBClientScala
import com.influxdb.client.scala.internal.InfluxDBClientScalaImpl
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.InfluxDBClient
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point

class InfluxDBWriter(url: String, token: String) extends LazyLogging {
  private val influxDBClient = InfluxDBClientFactory
    .create(url, token.toCharArray())

  def writePoint(point: Point): Boolean = {
    this.influxDBClient
      .getWriteApi()
      .writeMeasurement("bucket", "org", point.getPrecision(), point)
    return true
  }

  def close(): Unit = {
    this.influxDBClient.close()
  }
}
