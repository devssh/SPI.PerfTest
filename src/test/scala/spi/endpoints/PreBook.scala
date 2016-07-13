package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object PreBook {

  var getPrebook = http("get_pre_book")
    .get("/chennai/coming-soon/${movie_name}")
    .check(status.is(200))

  var createAndInstantPay = http("create_and_instant_pay")
    .post("/${coming_soon_id}/pre-book/create_and_instant_pay")
    .header("Accept","application/json")
    .formParam("orderOfPriority", "day,cinema,shownumber,experience")
    .formParam("source", "web")
    .formParam("noOfTickets", "1")
    .formParam("choicesPerBooking", "6")
    .formParam("shows", "${show1}")
    .formParam("shows", "${show2}")
    .formParam("shows", "${show3}")
    .formParam("shows", "${show4}")
    .formParam("termsAndConditions", "true").asFormUrlEncoded
    .check(status.is(200), jsonPath("$.redirectUrl").exists.saveAs("prebookOrderUrl"),jsonPath("$.error").notExists)

  var prebookOrderDetails = http("prebook_order_details")
    .get("${prebookOrderUrl}")
    .check(status.is(200))
}
