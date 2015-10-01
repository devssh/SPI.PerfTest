package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.DataSetup._
import spi.ScenarioChains._
import spi.endpoints.Cinemas._
import spi.utils.Properties._
import scala.concurrent.duration._

class InstantPay extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
    List( extraInfo.request,extraInfo.request.getUri ,extraInfo.session,extraInfo.request.getHeaders,extraInfo.request.getCookies,
      extraInfo.request.getFormParams,extraInfo.request.getQueryParams,
      extraInfo.response.headers,extraInfo.response.cookies,extraInfo.response.statusCode,extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect



  val instantPayFlow = scenario("walletInstantPayFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(quickBook)
    .exec(instantPay)

  val checkTicketFlow = scenario("check_ticket_flow").feed(movieFeeder).feed(quantityFeeder)
    .exec(browsingAvailability)


  setUp(
//    checkTicketFlow.inject(atOnceUsers(5000)),
    instantPayFlow.inject(constantUsersPerSec(100) during(60 second))
  ).protocols(httpConf)
}