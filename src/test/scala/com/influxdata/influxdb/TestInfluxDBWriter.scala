package com.influxdata.influxdb

import akka.actor.ActorSystem
import akka.stream.testkit.scaladsl.TestSink
import akka.stream.ActorMaterializer
import com.dimafeng.testcontainers.{
  ForAllTestContainer,
  GenericContainer,
  KafkaContainer,
  MultipleContainers
}
import com.influxdb.client.domain.Query
import com.influxdb.client.InfluxDBClient
import com.influxdb.client.scala.InfluxDBClientScalaFactory
import com.influxdb.client.write.Point
import com.influxdb.query.FluxRecord
import java.util.Properties
import org.apache.kafka.clients.KafkaClient
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{
  StringDeserializer,
  StringSerializer
}
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.containers.wait.Wait

class TestInfluxDBWriter extends AnyFlatSpec with ForAllTestContainer {
  implicit val system: ActorSystem = ActorSystem("it-tests")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val influxDBContainer = GenericContainer(
    "quay.io/influxdb/influxdb:2.0.0-beta",
    exposedPorts = Seq(9999),
    waitStrategy = Wait.forHttp("/ready")
  )

  override val container = MultipleContainers(influxDBContainer)

  "Writer" should "be able to write a Point to InfluxDB" in {
    val result = requests.post(
      "http://" + influxDBContainer.containerIpAddress + ":" + influxDBContainer
        .mappedPort(9999) + "/api/v2/setup",
      data = ujson.Obj(
        "username" -> "test",
        "password" -> "test5678",
        "token" -> "test",
        "org" -> "test",
        "bucket" -> "test"
      )
    )
    assert(result.statusCode == 201)

    val writer =
      new InfluxDBWriter(
        url =
          "http://" + influxDBContainer.containerIpAddress + ":" + influxDBContainer
            .mappedPort(9999),
        org = "test",
        token = "test",
        bucket = "test"
      )

    val written = writer.writePoint(
      Point.measurement("test").addTag("tag", "one").addField("field", 1)
    )

    assert(written == true)

    val influxDBClient = InfluxDBClientScalaFactory
      .create(
        "http://" + influxDBContainer.containerIpAddress + ":" + influxDBContainer
          .mappedPort(9999),
        "test".toCharArray()
      )

    val countQuery = ("from(bucket: \"test\")\n"
      + " |> range(start: -1m)")

    //Result is returned as a stream
    val results = influxDBClient
      .getQueryScalaApi()
      .query(countQuery, "test")
      .runWith(TestSink.probe[FluxRecord])

    val firstRecord = results.requestNext()

    assert(firstRecord.getMeasurement().equals("test"))
    assert(firstRecord.getValueByKey("tag") == "one")
    assert(firstRecord.getValueByKey("field") == 1)

    influxDBClient.close()
  }
}
