package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import assertions._
import bootstrap._

class ShowTimeSimulation extends Simulation {
  val httpConf = http
    .baseURL("http://192.168.17.65")
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
			"Cookie" -> """cityName=chennai""",
			"X-Requested-With" -> """XMLHttpRequest"""
  )

  val headers_12 = Map(
    		"Accept" -> """application/json, text/javascript, */*; q=0.01""",
    		"Cache-Control" -> """no-cache""",
    		"Content-Type" -> """application/json; charset=UTF-8;""",
            "Origin" -> """http://localhost""",
            "Referer" -> """http://localhost/chennai""",
            "Host" -> """localhost""",
            "Cookie" -> """cityName=chennai""",
    	    "X-Requested-With" -> """XMLHttpRequest"""
  )

    val movieFeeder = csv("movie_name.csv").random
    val userFeeder = csv("user_credentials.csv").random

     val scn = scenario("Show Times")

	    .exec(http("request_3")
			.get("/chennai/show-times/17-10-2013")
			.check(status.is(200))
		)
		.pause(500 milliseconds)

        .exec(http("request_5")
            .get("/account/logged")
            .headers(headers_5)
            .check(status.is(401))
        )
        .pause(500 milliseconds)

        .feed(userFeeder)
        .exec(http("request_6")
            .post("/account/authenticate")
            .headers(headers_6)
            .param("user", """${username}""")
            .param("password", """${password}""")
        .check(status.is(200)))
        .pause(500 milliseconds)

        .feed(movieFeeder)
        .exec(http("request_8")
		    .post("/chennai/ticket/${movie_name}/book")
			.param("""sessionId""", """${session_id}""")
     		.param("""seatCategory""", """ELITE""")
     		.param("""quantity""", """2"""))
     	.pause(1)

        .exec(http("request_12")
           .post("/screen/layout")
           .headers(headers_12)
           .body(StringBody("""{"sessionId":"${session_id}","quantity":"2","seatCategory":"ELITE"}""")).asJSON
           .check(status.is(200)))

        setUp(scn.inject(ramp(100 users) over (10 seconds))).protocols(httpConf)
}