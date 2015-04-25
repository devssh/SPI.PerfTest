package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.core.feeder.Record
import io.gatling.core.result.message.KO
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
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")

  val movieFeeder = csv("sessions.csv").random
  val userFeeder = csv("users.csv").random
  val quantityFeeder = csv("quantity.csv").random

  val recordsByDate: Map[String, IndexedSeq[Record[String]]] = csv("sessions.csv").records.groupBy{ record => record("date") }
  val sessionsByDate: Map[String, IndexedSeq[String]] = recordsByDate.mapValues{ records => records.map {record => record("session_id")} }

  val justPayFlow = scenario("justPayFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)
    .exec(createOrder)
    .exec(jusPayPayment)
    .exec(checkHistory)

  val fuelPayFlow = scenario("fuelPayFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)
    .exec(createOrder)
    .exec(fuelPayment)

  val cancelFlow = scenario("canceledFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)
    .exec(createOrder)
    .exec(cancelOrder)
    .exec(checkHistory)

  val checkTicketFlow = scenario("check_ticket_flow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)

  setUp(
    cancelFlow.inject(rampUsers(1) over (1 second))
//    ,checkTicketFlow.inject(rampUsers(5000) over (10 seconds)),
//    fuelPayFlow.inject(rampUsers(1000) over (10 seconds))
  ).protocols(httpConf)
}
