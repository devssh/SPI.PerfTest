package spicinemas

import io.gatling.core.Predef._
import io.gatling.core.feeder._
import spicinemas.EndPoints._
import io.gatling.jdbc.Predef._

object ScenarioChains {


  val sessionsQuery = "select movie_name as full_movie_name,slugged_movie_name as movie_name, session_id,cinema_name,date(start_time) as date,category from session_category_prices join sessions on session_id = sessions.id where start_time>CURRENT_DATE+1 and cinema_name != 'thecinema@BROOKEFIELDS' limit 500"
  val usersQuery = "select email from users where is_active='t' and password='c++0IkxtJVE=' limit 1000"

  private val dbUrl: String = "jdbc:postgresql://192.168.57.99:5432/spi_cinemas"
  private val userName: String = "postgres"
  val movieFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(dbUrl, userName,"",sessionsQuery).circular
  val userFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(dbUrl, userName,"",usersQuery).circular
  val quantityFeeder = csv("quantity.csv").random
  val recordsByDate: Map[Any, IndexedSeq[Record[Any]]] = movieFeeder.records.groupBy{ record => record("date") }
  val sessionsByDate: Map[Any, IndexedSeq[Any]] = recordsByDate.mapValues{ records => records.map {record => record("session_id")} }


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

  val home_page = scenario("home page")
                  .exec(homePage)
}
