package spicinemas

import io.gatling.core.Predef._
import spicinemas.EndPoints._


object ScenarioChains {
  val checkTicket = scenario("checkTickets")
    .exec(loggedUserCheck)
    .exec(homePage)
    .exec(userAuthentication)
    .exec(nowShowing)
    .exec(commingSoon)
    .exec(showTimes)
    .exec(movieAvailabilityForWeek)
    .exec(moviePage)
    .exec(sessionAvailability)

  val createOrder = scenario("createOrder")
    .exec(checkTicket)
    .exec(checkOrderExist)
    .exec(movieDetails)
    .exec(orderCreate)
    .exitHereIfFailed
    .exec(orderDetails)
    .exec(seatLayout)
    .exec(availableFood)
    //    .exec(makeFoodOrder)
    .exec(citrusBank)
    .exec(paymentOptions)

  val jusPayPayment = scenario("make justPay payment")
    .exec(paymentInitiate)
    .exec(paymentBannyan)
    .exec(activePromotions)
    .exec(payJustPay)
    .exec(orderConfirm)
    .exec(bookedTicket)
    .exec(staticTnC)

  val fuelPayment = scenario("make justPay payment")
    .exec(paymentInitiate)
    .exec(paymentBannyan)
    .exec(activePromotions)
    .exec(payJustPay)
    .exec(fuelPay)
    .exec(bookedTicket)
    .exec(staticTnC)

  val checkHistory = scenario("check history")
    .exec(bookedHistory)
    .exec(preOrderHistory)
}