package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.endpoints.Cinemas
import Cinemas._
import spi.ScenarioChains._
import spi.DataSetup._
import spi.utils.Properties._
import scala.concurrent.duration._

class EndToEnd extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
    List( extraInfo.request,extraInfo.request.getUri ,extraInfo.session,extraInfo.request.getHeaders,extraInfo.request.getCookies,
      extraInfo.request.getFormParams,extraInfo.request.getQueryParams,
      extraInfo.response.headers,extraInfo.response.cookies,extraInfo.response.statusCode,extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect

  val cancelFlow = scenario("canceledFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(createOrder)
    .exec(cancelOrder)

  val walletFlow = scenario("walletFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(createOrder)
    .exec(payInit)
    .exec(walletPayment)
    .exec(checkHistory)

  val checkTicketFlow = scenario("check_ticket_flow").feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)


  val checkHomePage = scenario("check_home_page").exec(home_page)

  setUp(
  checkTicketFlow.inject(atOnceUsers(60)),
    walletFlow.inject(atOnceUsers(60))
//  checkTicketFlow.inject(rampUsers(5000) over(50 seconds)),
//    walletFlow.inject(rampUsers(2000) over(50 seconds))
  ).protocols(httpConf)
}
