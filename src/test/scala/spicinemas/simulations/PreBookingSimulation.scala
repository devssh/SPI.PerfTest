package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import spicinemas.utils.Properties._

class PreBookingSimulation extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect
    .shareConnections

  val headerWebForm = Map(
    "Cache-Control" -> """no-cache""",
    "Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
    "Pragma" -> """no-cache""",
    "Cookie" -> "cityName=chennai"
  )

  val headerAjaxForm = Map(
    "Cache-Control" -> """no-cache""",
    "Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
    "Pragma" -> """no-cache""",
    "Cookie" -> "cityName=chennai",
    "X-Requested-With" -> """XMLHttpRequest"""
  )

  val movieName = "lingaa"
  val preferenceFeeder = csv("pre_booking.csv").random

  val scn = scenario("Login and Place a new PreBooking request")
    .exec(session => {
    import java.net.URI

    import com.ning.http.client._
    import io.gatling.http.cookie._

    val customCookie1 = new Cookie(domain, "cityName", "chennai", "/", 864000, false)
    val customCookie2 = new Cookie(domain, "selectedCity", "chennai", "/", 864000, false)
    val cookieStore = CookieJar(new URI(baseUrl), List(customCookie1, customCookie2))
    session.set("gatling.http.cookies", cookieStore)
  })
    .exec(http("authenticate")
    .post("/account/authenticate")
    .headers(headerAjaxForm)
    .param("user", "perf@test.com")
    .param("password", "twpass")
    .check(status.is(200)))
    .feed(preferenceFeeder)
    .exec(http("prebook")
        .post("/chennai/" + movieName + "/pre-book/create")
        .headers(headerWebForm)
        .param("preferredDates", """${preferredDate}""")
        .param("cinemas", """${cinema}""")
        .param("shows", """${show}""")
        .param("experience", """${experience}""")
        .param("noOfTickets", """${noOfTickets}""")
        .param("avoid", """${avoid}""")
        .param("termsAndConditions", "true")
        .check(status.is(303)))
  setUp(scn.inject(ramp(12000 users) over (300 seconds))).protocols(httpConf)
}
