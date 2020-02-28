package com.influxdata.influxdb

import scala.util.Try
import com.typesafe.scalalogging.LazyLogging

class InfluxDBWriter() extends LazyLogging {
  def writePoint(s: String): Boolean = {
    logger.info("Writing a point!" + s)
    return true
  }
}
