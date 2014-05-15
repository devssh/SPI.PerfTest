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

  val scn = scenario("Get now showing")
    .exec(
    http("request_1")
      .get("/chennai/now-showing")
      .check(
      css( """ul.movie__grid li.movie__grid__item:first-child a.filter-value""", "href").exists.saveAs("movie_url"),
      status.is(200)))

   .exec(
    http("request_2")
      .get("${movie_url}")
      .check(
      status.is(200),
      css(""" div.movie__title """).exists
    ))

  setUp(scn.inject(ramp(100 users) over (10 seconds))).protocols(httpConf)
}
