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
    // taskConfig.getString(TelegramSinkConfig.TELEGRAM_BOT_NAME) shouldBe "test-name"
    // taskConfig.getString(TelegramSinkConfig.TELEGRAM_BOT_USERNAME) shouldBe "test-user-name"
    // taskConfig
    //   .getPassword(TelegramSinkConfig.TELEGRAM_BOT_DESTINATION_CHAT_ID)
    //   .value shouldBe "-12345"
    // taskConfig
    //   .getPassword(TelegramSinkConfig.TELEGRAM_BOT_API_KEY)
    //   .value shouldBe "99999999:XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    taskConfig.getList(InfluxDBSinkConfig.TOPICS) shouldBe Seq(
      "test-sink-topic"
    ).asJava
  }
}
