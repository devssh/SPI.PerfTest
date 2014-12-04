package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import assertions._
import bootstrap._
import spicinemas.utils.Properties._

class NowShowingSimulation extends Simulation {

  val httpConf = http
    .baseURL(baseUrl)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect
    .shareConnections

val headers_5 = Map(
        "X-Requested-With" -> """XMLHttpRequest""",
        """Cookie""" -> """cityName=chennai"""
    )

val headers_6 = Map(
      "Cache-Control" -> """no-cache""",
      "Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
      "Pragma" -> """no-cache""",
      "Cookie" -> "cityName=chennai",
      "X-Requested-With" -> """XMLHttpRequest"""
  )

  val movieFeeder = csv("movie_name.csv").circular
  val userFeeder = csv("user_credentials.csv").circular

  val scn = scenario("Get now showing and show times")
    .exec(http("home page")
      .get("/")
      .check(status.is(200)))
    .pause(500 milliseconds)
    .exec(session => {
      import java.net.URI      
      import io.gatling.http.cookie._;
      import com.ning.http.client._;

      val customCookie1 = new Cookie(domain, "cityName", "chennai", "/", 864000, false)
      val customCookie2 = new Cookie(domain, "selectedCity", "chennai", "/", 864000, false)
      val cookieStore = CookieJar(new URI(baseUrl), List(customCookie1, customCookie2))
      session.set("gatling.http.cookies", cookieStore)
    })
    .exec(http("showtimes page page")
      .get("/chennai/show-times")
      .check(status.is(200)))
    .pause(500 milliseconds)
    .feed(userFeeder)
    .exec(http("account-logged")
        .get("/account/logged")
        .headers(headers_5)
        .check(status.is(401))
    )
    .pause(500 milliseconds)
    .exec(http("authenticate")
        .post("/account/authenticate")
        .headers(headers_6)
        .param("user", """${username}""")
        .param("password", """${password}""")
    .check(status.is(200)))
  setUp(scn.inject(ramp(10000 users) over (120 seconds))).protocols(httpConf)
}
