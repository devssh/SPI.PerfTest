
package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.DateTime


object Channels {

  var channelSeatLayout = http("channel seat layout")
    .get("/channel/justickets/seats/${session_id}")
    .header("Authorization","Bearer okyNycnkeH4hzKUzLWWTT1B9Uq94T5XqmstRkslsW8oAi6gQLBZwwNXVinENE8Kg")
    .header("Accept","application/json")
    .check(status.is(200))
}