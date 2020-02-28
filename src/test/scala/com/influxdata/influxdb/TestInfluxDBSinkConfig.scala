package com.influxdata.influxdb

import scala.collection.JavaConverters._

/**
  * Test class for {@link InfluxDBSinkConfig}.
  *
  */
class TestInfluxDBSinkConfig extends TestInfluxDBBase {
  test("A test InfluxDBSinkConfig should be correctly configured") {
    val config = getSinkConfig
    val taskConfig = new InfluxDBSinkConfig(config.asJava)
    taskConfig.getList(InfluxDBSinkConfig.TOPICS) shouldBe Seq(
      "test-sink-topic"
    ).asJava
  }
}
