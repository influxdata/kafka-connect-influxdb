package com.influxdata.influxdb

import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

trait TestInfluxDBBase extends FunSuite with Matchers with BeforeAndAfter {
  def getSinkConfig: Map[String, String] = {
    Map(
      InfluxDBSinkConfig.INFLUXDB_URL -> "http://influxdb:9999",
      InfluxDBSinkConfig.INFLUXDB_ORG -> "org",
      InfluxDBSinkConfig.INFLUXDB_TOKEN -> "auth_token",
      InfluxDBSinkConfig.INFLUXDB_BUCKET -> "my_bucket",
      InfluxDBSinkConfig.TOPICS -> "test-sink-topic"
    )
  }
}
