package com.influxdata.influxdb

import scala.util.Try
import com.typesafe.scalalogging.LazyLogging
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point

class InfluxDBWriter(url: String, org: String, token: String, bucket: String)
    extends LazyLogging {
  private val influxDBClient =
    InfluxDBClientFactory.create(url, token.toCharArray())

  def writePoint(point: Point): Boolean = {
    this.influxDBClient
      .getWriteApiBlocking()
      .writePoint(bucket, org, point)
    return true
  }

  def close(): Unit = {
    this.influxDBClient.close()
  }
}
