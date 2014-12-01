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

  val movieFeeder = csv("movie_name.csv").circular

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
    .exec(http("now showing page")
      .get("/chennai/now-showing")
      .check(status.is(200)))
    .pause(500 milliseconds)
   .feed(movieFeeder)
   .exec(http("show times page for a movie")
      .get("/chennai/now-showing/${movie_name}")
      .check(status.is(200)))
   .pause(500 milliseconds)
    .exec(http("get movie session")
      .get("/chennai/now-showing/${movie_name}/${date}")
      .check(status.is(200)))
    .pause(500 milliseconds)
    .exec(http("showtimes page page")
      .get("/chennai/show-times")
      .check(status.is(200)))

  setUp(scn.inject(ramp(10000 users) over (100 seconds))).protocols(httpConf)
}
