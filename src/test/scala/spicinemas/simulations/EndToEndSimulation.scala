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
    .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.response.statusCode,extraInfo.response.body,extraInfo.session))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
  .headers(cleanSessionHeader)


  val justPayFlow = scenario("justPayFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)
    .exec(createOrder)
    .exec(jusPayPayment)
//    .exec(checkHistory)

  val fuelPayFlow = scenario("fuelPayFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)
    .exec(createOrder)
    .exec(fuelPayment)

  val cancelFlow = scenario("canceledFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(createOrder)
    .exec(cancelOrder)

  val checkTicketFlow = scenario("check_ticket_flow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)

  val checkHistoryFlow = scenario("check_history_flow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(userAuthentication)
    .exitHereIfFailed
  .exec(checkHistory)

  val checkHomePage = scenario("check_home_page").exec(home_page)

  setUp(
    checkTicketFlow.inject(atOnceUsers(5000)),
    cancelFlow.inject( atOnceUsers(2000))
//    checkTicketFlow.inject(rampUsers(500) over (10 second))
   // checkHomePage.inject(atOnceUsers(10000))
  ).protocols(httpConf)
}