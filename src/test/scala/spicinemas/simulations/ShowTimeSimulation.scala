package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import assertions._
import bootstrap._
import spicinemas.utils.Properties._

class ShowTimeSimulation extends Simulation {
  val httpConf = http
    .baseURL(baseUrl)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect
    .shareConnections

  val domain = "devtest.spicinemas.in"
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

  val headers_12 = Map(
        "Accept" -> """application/json, text/javascript, */*; q=0.01""",
        "Cache-Control" -> """no-cache""",
        "Content-Type" -> """application/json; charset=UTF-8;""",
            "Origin" -> """http://devtest.spicinemas.in""",
            "Referer" -> """http://devtest.spicinemas.in/chennai""",
            "Host" -> """devtest.spicinemas.in""",
            "Cookie" -> """cityName=chennai""",
          "X-Requested-With" -> """XMLHttpRequest"""
  )

  val hearderForJsonGet = Map(
    		"Accept" -> """application/json, text/javascript, */*; q=0.01""",
    		"Cache-Control" -> """no-cache""",      
        "Cookie" -> """cityName=chennai"""
  )

    val movieFeeder = csv("movie_name.csv").random
    val userFeeder = csv("user_credentials.csv").random

    val scn = scenario("Show Times")    
    .exec(http("show-times")
			.get("/chennai/show-times/15-05-2014")
        .queryParam("seats","2")
			.check(status.is(200))
	  )    

		.pause(500 milliseconds)

    .exec(http("account-logged")
        .get("/account/logged")
        .headers(headers_5)
        .check(status.is(401))
    )
    .pause(500 milliseconds)
    .exec(session => {
      import java.net.URI      
      import io.gatling.http.cookie._;
      import com.ning.http.client._;

      val customCookie = new Cookie(domain, "cityName", "chennai", "/", 864000, false)
      val cookieStore = CookieJar(new URI(baseUrl), List(customCookie))
      session.set("gatling.http.cookies", cookieStore)
    })
    .feed(userFeeder)
    .exec(http("authenticate")
        .post("/account/authenticate")
        .headers(headers_6)
        .param("user", """${username}""")
        .param("password", """${password}""")
    .check(status.is(200)))

    .pause(500 milliseconds)

    .feed(movieFeeder)
    .exec(http("book")
      .post("/chennai/ticket/${movie_name}/book")
    	.param("""sessionId""", """${session_id}""")
   		.param("""seatCategory""", """ELITE""")
   		.param("""quantity""", """2""")
      .param("""cityName""","""chennai""")
      .check(status.is(200), css("""#orderId""","value").exists.saveAs("orderId"))
    )
 	  .pause(1000 milliseconds)

    // .exec(http("orderDetail")
    //   .get("/order/details")      
    //   .headers(hearderForJsonGet)              
    //   .queryParam("""sessionId""","""${session_id}""")
    //   .queryParam("""quantity""","""2""")
    //   .queryParam("""seatCategory""","""ELITE""")            
    //   .check(status.is(200)))      
    //   // .check(jsonPath("$.orderId").saveAs("orderId")))

  // .exec(http("layout")
  //    .post("/screen/layout")
  //    .headers(headers_12)
  //    .param("sessionId", """${session_id}""")
  //    .param("quantity", """2""")
  //    .param("seatCategory", """ELITE""")
  //    .param("orderId", """${orderId}""")
  //    .param("orderId", """false""")
  //    // .body(StringBody("""{"sessionId":"${session_id}","quantity":"2","seatCategory":"ELITE","orderId":"${orderId}"}""")).asJSON
  //    .check(status.is(200)))

  setUp(scn.inject(ramp(2000 users) over (5 seconds))).protocols(httpConf)
}