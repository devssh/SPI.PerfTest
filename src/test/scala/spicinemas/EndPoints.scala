package spicinemas

import io.gatling.core.Predef._
import io.gatling.http.Predef._


object EndPoints {
  val cleanSessionHeader = Map(
    "Cookie" -> "cityName=chennai"
  )

  val formHeader = Map(
    "Cookie" -> "cityName=chennai",
    "Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8"""
  )

  var homePage = http("home_page")
    .get("/")
    .headers(cleanSessionHeader)
    .check(status.is(200))

  var commingSoon = http("comming_soon")
    .get("/chennai/coming-soon")
    .check(status.is(200))

  var showTimes = http("show_times")
    .get("/chennai/show-times")
    .check(status.is(200))

  var movieAvailabilityForWeek = http("movie_availability")
    .post("/chennai/sessions/movie-availability")
    .body(StringBody( """{"sessionIds":["${session_id}"],"movieName":"${movie_name}"}""")).asJSON
    .check(status.is(200))

  var sessionsAvailability = http("sessions_availablity")
    .post("/chennai/sessions/session-availability")


  var userAuthentication  = http("login")
    .post("/account/authenticate")
    .body(StringBody("user=${email}&password=twpass"))
    .headers(formHeader)
    .check(status.is(200))

  var nowShowing  = http("now showing page")
    .get("/chennai/now-showing")
    .check(status.is(200))

  var moviePage = http("movie_page")
    .get("/chennai/now-showing/${movie_name}")
    .queryParam("seats", "${quantity}")
    .check(status.is(200))

  var loggedUserCheck = http("account-logged")
    .get("/account/logged")
    .headers(cleanSessionHeader)
    .check(status.in(List(401,200)))

  var checkOrderExist = http("order status")
    .post("/order/status")
    .body(StringBody( """{"sessionId":"${session_id}","quantity":"${quantity}","seatCategory":"${category}"}""")).asJSON
    .check(status.is(200))

  var orderCreate = http("order create")
    .post("/chennai/ticket/${movie_name}/book")
    .headers(formHeader)
    .body(StringBody("sessionId=${session_id}&seatCategory=${category}&quantity=${quantity}"))
    .check(status.is(200), jsonPath("$.orderId").exists.saveAs("orderId"))

  var bookingPage = http("booking page")
    .post("chennai/ticket/${movie_name}/booking-page")
    .body(StringBody("sessionId=${session_id}&orderId=${orderId}"))
    .check(status.is(200))

  var orderDetails = http("orderDetail")
    .get("/order/details")
    .queryParam( "sessionId", "${session_id}")
    .queryParam( "quantity", "${quantity}")
    .queryParam( "seatCategory", "${category}")
    .check(status.is(200))

  var seatLayout = http("layout")
    .post("/screen/layout")
    .body(StringBody( """{"sessionId":"${session_id}","quantity":"${quantity}","seatCategory":"${category}","orderId":"${orderId}","isAutoSelected":true}""")).asJSON
    .check(status.is(200))

  var cancelOrder = http("cancel")
    .post("/chennai/ticket/cancel")
    .body(StringBody( """{"sessionId":"${session_id}","selectedCity":"chennai","orderId":"${orderId}"}""")).asJSON
    .check(status.is(200))


  var paymentInitiate = http("payment initiate")
    .post("/payment/initiate")
    .body(StringBody("""{"source": "WWW","amount": 430,"paymentType": "juspay","orderId":"${orderId}"}""")).asJSON
    .check(status.is(200),jsonPath("$.paymentReferenceNumber").exists.saveAs("paymentReferenceNumber"))

  var orderConfirm =  http("order confirm")
    .get("/order/cc-confirm")
    .queryParam( "order_id", "${paymentReferenceNumber}")
    .queryParam( "status", "CHARGED")
    .queryParam( "status_id", "21")
    .check(status.is(303))

  var paymentOptions =  http("payment options")
    .get("/payment/options")
    .check(status.is(200))



  var availableFood   = http("food_availability")
    .post("/food")
    .body(StringBody("${cinema_name}")).asJSON
    .check(status.is(200))


  var makeFoodOrder   = http("make_food_order")
    .post("/food/buy")
    .body(StringBody("""{"orderId": "${orderId}",	"foodWithQty": {"10010247": 2,"10010347": 3}}""")).asJSON
    .check(status.is(200))

  var citrusBank   = http("citrus_bank")
    .get("/citrus/banks")
    .check(status.is(200))


  var paymentBannyan   = http("bannyan")
    .post("/payment/banyanAndTC")
    .body(StringBody("""{"banyan": true,"tc": true,"orderId": "${paymentReferenceNumber}"}""")).asJSON
    .check(status.is(200))


  var activePromotions  = http("active promotions")
    .get("/promotions/active")
    .check(status.is(200))

//
//  var promotionEnquire :  = http("payment options")
//    .get("/payment/options")
//    .check(status.is(200))


  var bookedHistory = http("booked-history")
    .get("/order/booked-history")
    .check(status.is(200))

  var bookedHistoryTicket   = http("payment options")
    .get("/order/booked-history/0")
    .check(status.is(200))


  var preOrderHistory  = http("payment options")
    .get("/payment/options")
    .check(status.is(200))

  var bookedTicket   =  http("booked ticket").get("/order/details/${orderId}").check(status.is(200))

  var payJustPay = http("pay citrus").post("/payment/juspay").body(StringBody("""{"orderId": "${orderId}"}""")).asJSON.check(status.is(200))

  var movieDetails   = http("movie_details").get("/movies/${movie_name}").check(status.is(200))

  var staticTnC   = http("static_t_n_c").get("/static/t-and-c").check(status.is(200))

  var fuelPay = http("fuel_pay").post("/fuel").body(StringBody("fuelCardNumber=9800000112223137&pin=1977&orderId=${orderId}&tc=true&banyan=true")).check(status.is(200))

  var sessionAvailability = http("session_availability")
    .post("/chennai/sessions/session-availability")
    .body(StringBody("""{"sessionIds": ["${session_id}"], "movieName": "${movie_name}""""))
    .check(status.is(200))

  var vistaLayout = http("blockTicketsAndGetSeatLayout")
    .post("/vista/blockTicketsAndGetSeatLayout").asJSON
    .body(StringBody( """{"sessionId":"${session_id}","quantity":1,"sessionStartDateTime":"${session_time}","ticketCode":"${ticket_code}", "vistaTransactionNumber":""}""")).asJSON



}
