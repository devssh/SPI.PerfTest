package spicinemas

import io.gatling.http.request.builder.{GetHttpRequestBuilder, PostHttpRequestBuilder}
import io.gatling.core.Predef._
import io.gatling.http.Predef._


object EndPoints {
  val cleanSessionHeader = Map(
    "Cookie" -> "cityName=chennai"
  )

  val formHeader = Map(
    "Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8"""
  )

  var userAuthentication: PostHttpRequestBuilder = http("login")
    .post("/account/authenticate")
    .param("user", "${email}")
    .param("password", "twpass")
    .headers(cleanSessionHeader)
    .check(status.is(200))

  var nowShowing: GetHttpRequestBuilder = http("now showing page")
      .get("/chennai/now-showing")
      .check(status.is(200))

  var nowShowingMovie: GetHttpRequestBuilder = http("show times page for a movie")
    .get("/chennai/now-showing/${movie_name}")
    .check(status.is(200))

  var nowShowingSession = http("get movie session")
    .get("/chennai/now-showing/${movie_name}/${date}")
    .queryParam("seats", "2")
    .check(status.is(200))

  var loggedUserCheck = http("account-logged")
    .get("/account/logged")
    .headers(cleanSessionHeader)
    .check(status.is(401))

  var orderStatus = http("order status")
    .post("/order/status")
    .body(StringBody( """{"sessionId":"${session_id}","quantity":"1","seatCategory":"ELITE"}""")).asJSON
    .check(status.is(200))

  var orderCreate = http("order create")
    .post("/chennai/ticket/${movie_name}/book")
    .headers(formHeader)
    .body(StringBody("sessionId=${session_id}&seatCategory=ELITE&quantity=1"))
    .check(status.is(200), jsonPath("$.orderId").exists.saveAs("orderId"))

  var orderDetails = http("orderDetail")
    .get("/order/details")
    .queryParam( "sessionId", "${session_id}")
    .queryParam( "quantity", "1")
    .queryParam( "seatCategory", "ELITE")
    .check(status.is(200))

  var seatLayout = http("layout")
    .post("/screen/layout")
    .body(StringBody( """{"sessionId":"${session_id}","quantity":"1","seatCategory":"ELITE","orderId":"${orderId}","isAutoSelected":false}""")).asJSON
    .check(status.is(200))

  var cancelOrder = http("cancel")
    .post("/chennai/ticket/cancel")
    .body(StringBody( """{"sessionId":"${session_id}","selectedCity":"chennai","orderId":"${orderId}"}""")).asJSON
    .check(status.is(200))


  var paymnetInitiate = http("payment initiate")
    .post("/payment/initiate")
    .body(StringBody("""{"source": "WWW","amount": 430,"paymentType": "juspay","orderId":"${orderId}"}""")).asJSON
    .check(status.is(200))

  var orderConfirm =  http("order confirm")
    .get("/order/cc-confirm")
    .queryParam( "order_id", "${orderId}")
    .queryParam( "status", "CHARGED")
    .queryParam( "status_id", "21")
    .check(status.is(200))

  var paymentOptions =  http("payment options")
    .get("/payment/options")
    .check(status.is(200))
}
