package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.EndPoints
import spi.EndPoints._
import spi.ScenarioChains._
import spi.utils.Properties._

class Auth extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
    List( extraInfo.request,extraInfo.request.getUri ,extraInfo.session,extraInfo.request.getHeaders,extraInfo.request.getCookies,
      extraInfo.request.getFormParams,extraInfo.request.getQueryParams,
      extraInfo.response.headers,extraInfo.response.cookies,extraInfo.response.statusCode,extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect



  val userFlow = scenario("canceledFlow").feed(userFeeder)
    .exec(loggedUserCheck)
    .exec(loginPage)
    .exec(userAuthentication)
    .exitHereIfFailed
    .repeat(5){exec(getAuthorizationToken).exec(setAuthCookie).exec(userInfo)}



  setUp(
    userFlow.inject(atOnceUsers(2000) )
  ).protocols(httpConf)
}