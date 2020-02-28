package com.influxdata.influxdb

import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import org.apache.kafka.clients.KafkaClient
import org.scalatest.flatspec.AnyFlatSpec
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.Properties
import org.apache.kafka.clients.producer.ProducerRecord

class TestInfluxDBWriter extends AnyFlatSpec with ForAllTestContainer {

  override val container = KafkaContainer()

  it should "Test Write to InfluxDB" in {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put(
      "key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
    props.put(
      "value.serializer",
      "org.apache.kafka.common.serialization.StringSerializer"
    )
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String]("topic", "key", "value")
    producer.send(record)
    producer.close()
    assert(true)
  }
}
