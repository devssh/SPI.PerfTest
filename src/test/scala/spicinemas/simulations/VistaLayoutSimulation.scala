package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spicinemas.utils.Properties._

import scala.concurrent.duration._

class VistaLayoutSimulation extends Simulation {
  val httpConf = http
    .baseURL(baseUrl)
    .disableFollowRedirect
    .shareConnections
    .disableCaching

  val headers_json = Map(
    "Content-Type" -> """application/json"""
  )


  val sessionFeeder = csv("session_details.csv").circular


  val scn = scenario("VistaLayout")
    .feed(sessionFeeder)
    .exec(http("blockTicketsAndGetSeatLayout")
    .post("/vista/blockTicketsAndGetSeatLayout")
    .headers(headers_json)
    .body(StringBody( """{"sessionId":"${session_id}","quantity":1,"sessionStartDateTime":"${session_time}","ticketCode":"${ticket_code}", "vistaTransactionNumber":""}""")).asJSON
    .check(status.is(200)))
    .exitHereIfFailed
  setUp(scn.inject(ramp(100 users) over (1 seconds))).protocols(httpConf)
}