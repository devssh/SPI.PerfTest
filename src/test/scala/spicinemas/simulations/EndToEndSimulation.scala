package spicinemas.simulations

import com.typesafe.scalalogging.slf4j.Logging
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.{PostHttpRequestBuilder, GetHttpRequestBuilder}
import spicinemas.EndPoints._
import spicinemas.utils.Properties._
import scala.concurrent.duration._
import io.gatling.core.structure.ChainBuilder._

class EndToEndSimulation extends Simulation with Logging {

  val httpConf = http
    .baseURL(baseUrl)
    .disableFollowRedirect
  //    .extraInfoExtractor((status:Status, session:Session, req:Request, resp:Response) => { List(req.getCookies.toString())})

  val movieFeeder = csv("sessions.csv").circular
  val userFeeder = csv("users.csv").circular



  val scn = scenario("E2E")
    .feed(userFeeder)
    .feed(movieFeeder)
    .exec(loggedUserCheck)
    .exec(homePage)
    .exec(userAuthentication)
    .exec(nowShowing)
    .exec(commingSoon)
    .exec(showTimes)
    .exec(movieAvailabilityForWeek)
    .exec(moviePage)
    .exec(movieAvailabilityForSession)
    .exec(movieDetails)
    .exec(orderCreate)
    .exec(orderDetails)
    .exec(seatLayout)
    .exec(availableFood)
    .exec(makeFoodOrder)
    .exec(citrusBank)
    .exec(paymentOptions)
    .exec(paymentInitiate)
    .exec(paymentBannyan)
    .exec(activePromotions)
    .exec(payJustPay)
    .exec(orderConfirm)
    .exec(bookedTicket)
    .exec(bookedHistory)
    .exec(preOrderHistory)




  setUp(scn.inject(ramp(1 users) over (1 seconds))).protocols(httpConf)
}
