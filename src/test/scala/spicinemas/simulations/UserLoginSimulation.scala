package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spicinemas.utils.Properties._

class UserLoginSimulation extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect
    .shareConnections

  val userFeeder = csv("user_credentials.csv").random

  val scn = scenario("Get now showing")
    .feed(userFeeder)
    .exec(
    http("request_1")
      .post("/account/authenticate")
      .param("user", """${username}""")
      .param("password", """${password}""")
      .check(status.is(200)))
  setUp(scn.inject(ramp(200 users) over (20 seconds))).protocols(httpConf)
}
