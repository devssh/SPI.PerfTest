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

  before {
    redisClient.flushall
  }



  val userLogin = scenario("Authentication").feed(userFeeder)
    .exec(loggedUserCheck)
    .exec(loginPage)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .exitHereIfFailed
    .exec(storeAuthTokenToRedis)


  val getUserInfo = scenario("UserInfo").feed(authTokenFeeder).
    exitHereIfFailed.
    exec(setAuthCookie)
    .repeat(5){exec(userInfo)}


  setUp(
    userLogin.inject(rampUsers(2200) over (100 seconds)),
    getUserInfo.inject(nothingFor(200),atOnceUsers(2000))
//    userLogin.inject(rampUsers(20) over (10 seconds)),
//        getUserInfo.inject(nothingFor(20 seconds),atOnceUsers(15))
  ).protocols(httpConf)
}