package spi

import io.gatling.core.Predef._
import io.gatling.core.feeder._
import spi.EndPoints._
import io.gatling.jdbc.Predef._

object ScenarioChains {


  val sessionsQuery = "select movie_name as full_movie_name,slugged_movie_name as movie_name, session_id,cinema_name,date(start_time) as date,category, food_items.id as food_id " +
    "from session_category_prices " +
    "join sessions on session_id = sessions.id " +
    "JOIN cinemas ON lower(cinemas.name)=lower(sessions.cinema_name) " +
    "JOIN food_location_mapping ON food_location_mapping.location_id=cinemas.id AND food_location_mapping.is_active=true " +
    "JOIN food_groups on food_groups.id=food_location_mapping.group_id AND food_groups.is_active=true " +
    "JOIN food_items ON food_items.group_id=food_groups.id  AND food_items.is_active=true " +
    "where start_time>CURRENT_DATE+1 and sessions.cinema_name != 'thecinema@BROOKEFIELDS'  and food_items.is_active='t' " +
    "limit 500"
  val usersQuery = "select email from users where is_active is true and password='yd+8vQnf2ajO3RZxAecJXw==' limit 10000"

  private val cinemasDbUrl: String = "jdbc:postgresql://192.168.57.106:9999/spi_cinemas"
  private val authDbUrl: String = "jdbc:postgresql://192.168.57.106:9999/auth"
  private val userName: String = "postgres"
  val movieFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(cinemasDbUrl, userName,"",sessionsQuery).circular
  val userFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(authDbUrl, userName,"",usersQuery).circular
  val quantityFeeder = csv("quantity.csv").random


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
    .exec(checkOrderExist)
    .exec(setAuthCookie)
    .exec(orderCreate)
    .exitHereIfFailed
    .exec(setAuthCookie)
    .exec(orderDetails)
    .exec(setAuthCookie)
    .exec(seatLayout)
    .exec(availableFood)
//    .exec(makeFoodOrder)
    .exec(setAuthCookie)
    .exec(citrusBank)
    .exec(setAuthCookie)
    .exec(paymentStart)
    .exitHereIfFailed
    .exec(setAuthCookie)
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
