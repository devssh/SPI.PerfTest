package spi

import io.gatling.core.Predef._
import spi.endpoints.Cinemas._
import spi.endpoints.Oauth._
import spi.endpoints.Payment._


object ScenarioChains {

  val browsingAvailability = scenario("checkTickets")
    .exec(homePage)
//    .rendezVous(5000)
    .exec(nowShowing)
    .exec(commingSoon)
    .exec(showTimes)
    .exec(movieAvailabilityForWeek)
    .exec(moviePage)
    .exec(price)

  val visitMoviePage = scenario("visitMoviePage")
    .exec(nowShowing)
    .rendezVous(10000)
    .repeat(5){
      exec(movieAvailabilityForWeek)
      .exec(moviePage)
      .exec(movie)
    }


  val createOrder = scenario("createOrder")
    .exec(loggedUserCheck)
//    .rendezVous(2000)
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

  val quickBook = scenario("createOrder")
//    .exec(loggedUserCheck)
    .exec(loginPage)
    .exec(userAuthentication)
    .exitHereIfFailed
    .exec(getAuthorizationToken)
    .exitHereIfFailed
    .exec(setAuthCookie)
    .exec(orderStatus)
    .exec(autoSelect)
    .exec(orderCreate)
    .exec(orderDetails)
    .exitHereIfFailed
    .exec(availableFood)
    
  val payInit = scenario("paymentOption")
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
    .exec(payThroughWallet)

  val checkHistory = scenario("check history")
    .exec(bookedHistory)
    .exec(bookedHistoryList)
//    .exec(preBookHistory)
//    .exec(preBookHistoryList)

  val home_page = scenario("home page")
                  .exec(homePage)
}
