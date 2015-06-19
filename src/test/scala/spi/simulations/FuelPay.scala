package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.EndPoints
import EndPoints._
import spi.ScenarioChains._
import spi.utils.Properties._


class FuelPay extends Simulation{

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo => List(extraInfo.response,extraInfo.response.statusCode,extraInfo.response.body,extraInfo.session))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .headers(cleanSessionHeader)

  val fuelPayFlow  = scenario("justPayFlow").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
  .exec(homePage)
    .exec(nowShowing)
    .exec(commingSoon)
    .exec(showTimes)
    .exec(movieAvailabilityForWeek)
    .exec(moviePage)
    //    .exec(sessionAvailability)
    .exec(price)
    .exec(loggedUserCheck)
    .exec(userAuthentication)
    .exitHereIfFailed
    .exec(checkOrderExist)
    .exec(quick_book)
    .exec(orderCreate)
    .exitHereIfFailed
    .exec(orderDetails)
    .exec(seatLayout)
    .exec(availableFood)
    .exec(makeFoodOrder)
    .exec(citrusBank)
    .exec(paymentOptions)
    .exec(fuelPay)
    .exec(bookedTicket)
    .exec(staticTnC)

 setUp(
   fuelPayFlow.inject(atOnceUsers(10)
   )).protocols(httpConf)
}
