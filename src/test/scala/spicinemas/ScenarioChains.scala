package spicinemas

import io.gatling.core.Predef._
import io.gatling.core.feeder._
import spicinemas.EndPoints._

object ScenarioChains {

  val movieFeeder = csv("sessions.csv").circular
  val userFeeder = csv("users.csv").circular
  val quantityFeeder = csv("quantity.csv").random

  val recordsByDate: Map[String, IndexedSeq[Record[String]]] = csv("sessions.csv").records.groupBy{ record => record("date") }
  val sessionsByDate: Map[String, IndexedSeq[String]] = recordsByDate.mapValues{ records => records.map {record => record("session_id")} }

  

  val browsingAvailability = scenario("checkTickets")
    .exec({session =>
    session("date").validate[String].map { date =>
      val ses = sessionsByDate(date).toArray.mkString("\"","\",\"","\"")
      session.set("session_ids", ses)
    }
  })
    .exec(homePage)
    .exec(nowShowing)
    .exec(commingSoon)
    .exec(showTimes)
    .exec(movieAvailabilityForWeek)
    .exec(moviePage)
    .exec(sessionAvailability)
    .exec(price)

  val createOrder = scenario("createOrder")
    .exec(loggedUserCheck)
    .exec(userAuthentication)
    .exitHereIfFailed
    .exec(checkOrderExist)
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
    .exec(payJustPay)
    .exec(fuelPay)
    .exec(bookedTicket)
    .exec(staticTnC)

  val checkHistory = scenario("check history")
    .exec(bookedHistory)
    .exec(bookedHistoryList)
  .exec(preBookHistory)
  .exec(preBookHistoryList)
}
