package spi.simulations

import io.gatling.core.Predef._
import io.gatling.core.validation.Validation
import io.gatling.http.Predef._
import spi.EndPoints._
import spi.DataSetup._
import spi.utils.Properties._
import scala.concurrent.duration._

class Auth extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
    List( extraInfo.request,extraInfo.session,
      extraInfo.response.headers,extraInfo.response.cookies,extraInfo.response.statusCode,extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect

  val userLogin = scenario("Authentication").feed(userFeeder)
    .exec(loggedUserCheck)
    .exec(loginPage)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .exitHereIfFailed
    .rendezVous(4998)
    .exec(userInfo)
    .pause(10 second)
    .exec(userInfo)
    .pause(10 second)
    .exec(userInfo)
    .pause(10 second)
    .exec(userInfo)
    .pause(10 second)
    .exec(userInfo)
    .pause(10 second)

  setUp(
    userLogin.inject(rampUsers(5000) over (20 seconds))
  ).protocols(httpConf)
}