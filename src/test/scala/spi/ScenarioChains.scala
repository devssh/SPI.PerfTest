package spi

import io.gatling.core.Predef._
import io.gatling.core.feeder._
import spi.EndPoints._
import io.gatling.jdbc.Predef._

object ScenarioChains {

  val browsingAvailability = scenario("checkTickets")
    .exec(homePage)
    .exec(nowShowing)
    .exec(commingSoon)
    .exec(showTimes)
    .exec(movieAvailabilityForWeek)
    .exec(moviePage)
    .exec(price)

  val createOrder = scenario("createOrder")
    .exec(loggedUserCheck)
    .exec(loginPage)
    .exec(userAuthentication)
    .exitHereIfFailed
    .exec(getAuthorizationToken)
    .exitHereIfFailed
    .exec(setAuthCookie)
    .exec(orderStatus)
    .exec(orderCreate)
    .exitHereIfFailed
    .exec(orderDetails)
    .exec(seatLayout)
    .exitHereIfFailed
    .exec(availableFood)
//    .exec(makeFoodOrder)
    .exec(citrusBank)
    .exec(paymentStart)
    .exitHereIfFailed
    .exec(paymentOptions)

  val jusPayPayment = scenario("make justPay payment")
    .exec(paymentInitiate)
    .exec(paymentBannyan)
    .exec(activePromotions)
    .exec(payJustPay)
    .exec(orderConfirm)
    .exec(bookedTicket)
    .exec(staticTnC)


  val walletPayment = scenario("make wallet payment")
    .exec(walletBalance)
    .exec(walletPay)

  val checkHistory = scenario("check history")
    .exec(bookedHistory)
    .exec(bookedHistoryList)
    .exec(preBookHistory)
    .exec(preBookHistoryList)

  val home_page = scenario("home page")
                  .exec(homePage)
}
