package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object PreBook {

  var getPrebook = http("get_pre_book")
    .get("/chennai/coming-soon/${prebook_movie_name}")
    .check(status.is(200))

  var createAndInstantPay = http("create_and_instant_pay")
    .post("/${prebook_id}/pre-book/create_and_instant_pay")
    .header("Accept","application/json")
    .formParam("orderOfPriority", "day,cinema,shownumber,experience")
    .formParam("noOfTickets", "1")
    .formParam("avoid", "true")
    .formParam("cinemas", "Escape")
    .formParam("shows", "2")
    .formParam("experience", "RDX")
    .formParam("termsAndConditions", "true")
    .formParam("preferredDays", "1").asFormUrlEncoded
    .check(status.is(200), jsonPath("$.redirectUrl").exists.saveAs("prebookOrderUrl"),jsonPath("$.error").notExists)

  var prebookOrderDetails = http("prebook_order_details")
    .get("${prebookOrderUrl}")
    .check(status.is(200))
}
