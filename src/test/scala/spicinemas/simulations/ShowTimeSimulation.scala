package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import assertions._
import bootstrap._

class ShowTimeSimulation extends Simulation {
  val httpConf = http
    .baseURL("http://10.16.3.236")
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect
    .shareConnections

  val headers_5 = Map(
  			"X-Requested-With" -> """XMLHttpRequest"""
  	)

  val headers_6 = Map(
			"Cache-Control" -> """no-cache""",
			"Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
			"Pragma" -> """no-cache""",
			"X-Requested-With" -> """XMLHttpRequest"""
  )

    val movieFeeder = csv("movie_name.csv").random
    val userFeeder = csv("user_credentials.csv").random

      val scn = scenario("Show Times")

	    .exec(http("request_3")
					.get("/chennai/show-times/08-10-2013")
					.check(status.is(200))
			)
		.pause(4)
        .exec(http("request_5")
        	.get("/account/logged")
        	.headers(headers_5)
        	.check(status.is(401))
        )
        .pause(13)
            .feed(userFeeder)
            .exec(http("request_6")
            	.post("/account/authenticate")
            	.headers(headers_6)
            	.param("user", "${username}")
            	.param("password", "${password}")
            .check(status.is(200)))

        .pause(27 milliseconds)

        .feed(movieFeeder)
        .exec(http("request_8")
		    .post("/chennai/ticket/${movie_name}/book")
			.param("""sessionId""", """${session_id}""")
     		.param("""seatCategory""", """ELITE""")
     		.param("""quantity""", """2"""))

        setUp(scn.inject(ramp(800 users) over (10 seconds))).protocols(httpConf)
}