package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.DataSetup._
import spi.endpoints.Oauth._
import spi.endpoints.PreBook._
import spi.utils.Properties._
import scala.concurrent.duration._

class PreBook extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
      List(extraInfo.request, extraInfo.session,
        extraInfo.response.headers, extraInfo.response.cookies, extraInfo.response.statusCode, extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect

  val prebookOrder = scenario("Authentication").feed(userFeeder)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .exitHereIfFailed
    .exec(getPrebook)
    .exec(createAndInstantPay)
    .exec(prebookOrderDetails)


  setUp(
    prebookOrder.inject(atOnceUsers(1))
  ).protocols(httpConf)
}