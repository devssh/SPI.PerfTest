package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spicinemas.EndPoints._
import scala.concurrent.duration._
import spicinemas.utils.Properties._

class UserLoginSimulation extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .disableFollowRedirect
    .shareConnections

  val userFeeder = csv("user_credentials.csv").random

  val scn = scenario("User Authentication")
    .feed(userFeeder)
    .exec(userAuthentication)

  setUp(scn.inject(ramp(1 users) over (1 seconds))).protocols(httpConf)

}
