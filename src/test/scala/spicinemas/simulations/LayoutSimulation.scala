package spicinemas.simulations

import com.typesafe.scalalogging.slf4j.Logging
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spicinemas.EndPoints._
import spicinemas.utils.Properties._
import scala.concurrent.duration._

class LayoutSimulation extends Simulation with Logging {

  val httpConf = http
    .baseURL(baseUrl)
    .disableFollowRedirect
    .shareConnections
//    .extraInfoExtractor((status:Status, session:Session, req:Request, resp:Response) => { List(req.getCookies.toString())})

  val movieFeeder = csv("sessions.csv").circular
  val userFeeder = csv("users.csv").circular


  val scn = scenario("Layout")
    .exec(loggedUserCheck)
    .pause(500 milliseconds)
    .feed(userFeeder)
    .exec(userAuthentication)
    .exitHereIfFailed
    .pause(200 milliseconds)
    .feed(movieFeeder)
    .exec(orderStatus)
    .exec(orderCreate)
    .exitHereIfFailed
    .pause(1 second)
    .exec(orderDetails)
    .exitHereIfFailed
    .exec(seatLayout)
    .exitHereIfFailed
    .pause(1 second)
    .exec(cancelOrder)
//  .exec(paymnetInitiate)
//  .exec(orderConfirm)

  setUp(scn.inject(ramp(6000 users) over (60 seconds))).protocols(httpConf)
}