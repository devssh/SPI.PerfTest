
package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.DateTime


object Cinemas {

  val cityCookie: (String, String) = "Cookie" -> "cityName=chennai"

  val formatter =  new java.text.SimpleDateFormat("dd-MM-yyyy")
  val tomorrowDate = formatter.format((new DateTime).plusDays(1).toDate)

  var homePage = http("home_page")
    .get("/")
    .header(cityCookie._1,cityCookie._2)
    .check(status.is(200))

  var setAuthCookie = addCookie(Cookie("spi_access_token","${authToken}"))
  var setWalletAuthCookie = addCookie(Cookie("spi_access_token","zZ99T9rYWTVEBtKH74aqopz1DQqLEUBWtZYbwoZniNwTBxAvOvAKcF8aZ0WwA9Ax"))

  var commingSoon = http("comming_soon")
    .get("/chennai/coming-soon/")
    .check(status.is(200))

  var showTimes = http("show_times")
    .get("/chennai/show-times/" + tomorrowDate)
    .queryParam("seats",2)
    .check(status.is(200))

  var movieAvailabilityForWeek = http("movie_availability")
    .post("/chennai/sessions/movie-availability")
    .body(StringBody( """{"sessionIds":["${session_id}"],"movieName":"${movie_name}"}""")).asJSON
    .check(status.is(200))

  var nowShowing  = http("now showing page")
    .get("/chennai/now-showing")
    .check(status.is(200))

  var moviePage = http("movie_page")
    .get("/chennai/now-showing/${movie_name}/")
    .check(status.is(200))

  var orderStatus = http("order status")
    .post("/order/status")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody( """{"sessionId":"${session_id}","quantity":"${quantity}","seatCategory":"${category}"}""")).asJSON
    .check(status.is(200))



  var orderCreate = http("order create")
    .post("/chennai/ticket/${movie_name}/book")
    .header(cityCookie._1,cityCookie._2)
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody("sessionId=${session_id}&seatCategory=${category}&quantity=${quantity}")).asFormUrlEncoded
    .check(status.is(200), jsonPath("$.orderId").exists.saveAs("orderId"))

  var paymentStart = http("payment start")
  .post("/order_payment/start")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody("""{"source": "WWW","orderId": "${orderId}"}""")).asJSON
  .check(status.in(200),jsonPath("$.paymentRequestId").exists.saveAs("paymentRequestId"))

  var bookingPage = http("booking page")
    .post("chennai/ticket/${movie_name}/booking-page")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody("sessionId=${session_id}&orderId=${orderId}"))
    .check(status.is(200))

  var orderDetails = http("order_detail")
    .get("/order/fetch_details")
    .header("Authorization","Bearer ${authToken}")
    .queryParam( "orderId", "${orderId}")
    .check(status.is(200))

  var seatLayout = http("layout")
    .post("/screen/layout")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody( """{"sessionId":"${session_id}","quantity":"${quantity}","seatCategory":"${category}","orderId":"${orderId}","isAutoSelected":false}""")).asJSON
    .check(status.is(200))

  var cancelOrder = http("cancel")
    .post("/chennai/ticket/cancel")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody( """{"sessionId":"${session_id}","selectedCity":"chennai","orderId":"${orderId}"}""")).asJSON
    .check(status.is(200))

  var orderConfirm =  http("order_confirm")
    .get("/order/cc-confirm")
    .queryParam( "order_id", "${paymentReferenceNumber}")
    .queryParam( "status", "CHARGED")
    .header("Authorization","Bearer ${authToken}")
    .queryParam( "status_id", "21")
    .check(status.in(List(303,200)))

  var availableFood  = http("food_availability")
    .post("/food")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody("${cinema_name}")).asJSON
    .check(status.is(200))


  var makeFoodOrder  = http("make_food_order")
    .post("/food/buy")
    .body(StringBody("""{"orderId": "${orderId}",	"foodWithQty": {"${food_id}": 2}}""")).asJSON
    .check(status.is(200))

  var activePromotions  = http("active promotions")
    .get("/promotions/active")
    .check(status.is(200))


  var bookedHistory = http("booked-history")
    .get("/order/booked-history")
    .check(status.is(200))


  var bookedHistoryList  = http("booked_history_list")
    .get("/order/booked-history/0")
    .check(status.is(200))

  var preBookHistory  = http("pre_book_history")
    .get("/pre-book/order/history/")
    .check(status.is(200))

  var preBookHistoryList  = http("pre_book_history_list")
    .get("/pre-book/order/history/0")
    .check(status.is(200))

  var bookedTicket   =  http("booked_ticket").get("/order/details/${orderId}").check(status.is(200))

  var walletBalance =  http("wallet_balance").get("/wallet/fetch_balance").check(status.is(200))

  var movieDetails   = http("movie_details").get("/movies/${full_movie_name}").check(status.is(200))

  var staticTnC   = http("static_t_n_c").get("/static/t-and-c").check(status.is(200))

  var price   = http("price").get("/chennai/ticket/prices").queryParam("sessionId","${session_id}".replaceAll(" ","+")).check(status.is(200))


  var sessionAvailability = http("session_availability")
    .post("/chennai/sessions/session-availability").asJSON
    .body(StringBody("""{"sessionIds": [${session_id}],"movieName": "${movie_name}"}"""))
    .check(status.is(200))

  var autoSelect = http("auto_select_seat").post("/chennai/ticket/${movie_name}/auto-select")
  .header("Authorization","Bearer ${authToken}")
  .body(StringBody("""{"sessionId":"${session_id}","seatCategory":"${category}","movieName":"${movie_name}","quantity":${quantity},"selectedCity":"chennai","isAutoSelected":true}"""))
    .asJSON.check(status.is(200))

  var instantPay = http("instant_pay").post("/order/buy_food_and_pay_instantly")
    .body(StringBody("""foodRequestJson=%7B%22orderId%22:%22${orderId}%22,%22foodWithQty%22:%7B%7D%7D"""))
    .asFormUrlEncoded.check(status.is(303))

  var vistaLayout = http("blockTicketsAndGetSeatLayout")
    .post("/vista/blockTicketsAndGetSeatLayout").asJSON
    .body(StringBody( """{"sessionId":"${session_id}","quantity":1,"sessionStartDateTime":"${session_time}","ticketCode":"${ticket_code}", "vistaTransactionNumber":""}""")).asJSON


}
