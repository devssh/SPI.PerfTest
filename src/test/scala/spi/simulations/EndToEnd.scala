package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.EndPoints
import EndPoints._
import spi.ScenarioChains._
import spi.utils.Properties._

class EndToEnd extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.response.statusCode,extraInfo.response.body,extraInfo.session))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect

  val cancelFlow = scenario("canceledFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(createOrder)
    .exec(cancelOrder)

  val checkTicketFlow = scenario("check_ticket_flow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)


  val checkHomePage = scenario("check_home_page").exec(home_page)

  setUp(
    checkTicketFlow.inject( atOnceUsers(5000)),
    cancelFlow.inject(atOnceUsers(2000) )
  ).protocols(httpConf)
}