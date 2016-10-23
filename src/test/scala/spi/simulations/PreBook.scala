package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.DataSetup._
import spi.endpoints.Oauth._
import spi.endpoints.PreBook._
import spi.endpoints.Cinemas._
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

  var noOfUsers = 5000;

  val prebookOrder = scenario("Authentication").feed(userFeeder).feed(preBookFeeder)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .rendezVous(noOfUsers)
    .exitHereIfFailed
    .exec(commingSoon)
    .rendezVous(noOfUsers)
    .exec(getPrebook)
    .rendezVous(noOfUsers)
    .exec(createAndInstantPay)
    .exitHereIfFailed
    .exec(prebookOrderDetails)


  val justPreBookOrder = scenario("Authentication").feed(userFeeder).feed(preBookFeeder)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .exec(createAndInstantPay)


  setUp(
//    prebookOrder.inject(atOnceUsers(3000))
    prebookOrder.inject(rampUsers(noOfUsers) over(10 seconds))
//    prebookOrder.inject(constantUsersPerSec(1) during(1 second))
  ).protocols(httpConf)
}