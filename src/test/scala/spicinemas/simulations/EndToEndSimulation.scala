package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.core.feeder.Record
import io.gatling.http.Predef._
import spicinemas.EndPoints
import EndPoints._
import spicinemas.ScenarioChains._
import spicinemas.utils.Properties._
import scala.concurrent.duration._

class EndToEndSimulation extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .disableFollowRedirect
    .extraInfoExtractor(extraInfo => List(extraInfo.response.bodyLength))

  val movieFeeder = csv("sessions.csv").random
  val userFeeder = csv("users.csv").random
  val quantityFeeder = csv("quantity.csv").random

  val recordsByDate: Map[String, IndexedSeq[Record[String]]] = csv("sessions.csv").records.groupBy{ record => record("date") }
  val sessionsByDate: Map[String, IndexedSeq[String]] = recordsByDate.mapValues{ records => records.map {record => record("session_id")} }

  val justPayFlow = scenario("justPayFlow")
    .feed(userFeeder)
    .feed(movieFeeder)
    .feed(quantityFeeder)
    .exec(checkTicket)
    .exec(createOrder)
    .exec(jusPayPayment)
    .exec(checkHistory)

  val fuelPayFlow = scenario("fuelPayFlow")
    .feed(userFeeder)
    .feed(movieFeeder)
    .feed(quantityFeeder)
    .exec(checkTicket)
    .exec(createOrder)
    .exec(jusPayPayment)
    .exec(checkHistory)

  val cancelFlow = scenario("canceledFlow")
    .feed(userFeeder)
    .feed(movieFeeder)
    .feed(quantityFeeder)
    .exec(checkTicket)
    .exec(createOrder)
    .exec(cancelOrder)

  val checkTicketFlow = scenario("check_ticket_flow")
    .feed(userFeeder)
    .feed(movieFeeder)
    .feed(quantityFeeder)
    .exec(checkTicket)

  setUp(
    fuelPayFlow.inject(atOnceUsers(1))
    ,checkTicketFlow.inject(rampUsers(1) over (1 seconds))
    ,cancelFlow.inject(rampUsers(1) over (1 seconds))
  ).protocols(httpConf)
}
