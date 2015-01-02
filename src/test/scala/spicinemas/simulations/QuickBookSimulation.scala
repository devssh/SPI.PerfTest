package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spicinemas.utils.Properties._

import scala.concurrent.duration._

class QuickBookSimulation extends Simulation {
  val httpConf = http
    .baseURL(baseUrl)
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .disableFollowRedirect
    .shareConnections

  val currentDate = "19-05-2014";
  val headerAjax = Map(
  			"X-Requested-With" -> """XMLHttpRequest""",
        """Cookie""" -> """cityName=chennai"""
  	)

  val headerAjaxForm = Map(
			"Cache-Control" -> """no-cache""",
			"Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
			"Pragma" -> """no-cache""",
			"Cookie" -> "cityName=chennai",
			"X-Requested-With" -> """XMLHttpRequest"""
  )

  val headerWebForm = Map(
      "Cache-Control" -> """no-cache""",
      "Content-Type" -> """application/x-www-form-urlencoded; charset=UTF-8""",
      "Pragma" -> """no-cache""",
      "Cookie" -> "cityName=chennai"
  )

  val headerAjaxJson = Map(
        "Accept" -> """application/json, text/javascript, */*; q=0.01""",
        "Cache-Control" -> """no-cache""",
        "Content-Type" -> """application/json; charset=UTF-8;""",
            "Origin" -> s"""http://${domain}""",
            "Referer" -> s"""http://${domain}/chennai""",
            "Host" -> s"""${domain}""",
            "Cookie" -> """cityName=chennai""",
          "X-Requested-With" -> """XMLHttpRequest"""
  )

  val hearderForJsonGet = Map(
    		"Accept" -> """application/json, text/javascript, */*; q=0.01""",
        "Content-Type" -> """application/json;""",
        "X-Requested-With" -> """XMLHttpRequest"""
  )

    val movieFeeder = csv("movie_name_2.csv").circular
    val userFeeder = csv("user_credentials_2.csv").circular

    val scn = scenario("quick book")
    .exec(http("account-logged")
        .get("/account/logged")
        .headers(headerAjax)
        .check(status.is(401))
    )
    .pause(500 milliseconds)
    .exec(session => {
      import java.net.URI

import com.ning.http.client._
      import io.gatling.http.cookie._

      val customCookie1 = new Cookie(domain, "cityName", "chennai", "/", 864000, false)
      val customCookie2 = new Cookie(domain, "selectedCity", "chennai", "/", 864000, false)
      val cookieStore = CookieJar(new URI(baseUrl), List(customCookie1, customCookie2))
      session.set("gatling.http.cookies", cookieStore)
    })
    .feed(userFeeder)
    .exec(http("authenticate")
        .post("/account/authenticate")
        .headers(headerAjaxForm)
        .param("user", """${username}""")
        .param("password", """${password}""")
    .check(status.is(200)))
    .exec(http("get sessions")
      .get("/chennai/sessions")
      .check(status.is(200))
    )
    .exitHereIfFailed
    .feed(movieFeeder)
      .exec(session => {
      println(session.attributes.get("session_id"))
      session
    })
    .exec(http("get movie session")
      .get("/chennai/now-showing/${movie_name}/${date}?seats=1")
      .check(status.is(200), css("""li[data-session-id='${session_id}'].available""").exists)
    )
    .exitHereIfFailed
    .exec(http("order status")
        .post("/order/status")
        .headers(headerAjaxJson)
        .body(StringBody("""{"sessionId":"${session_id}","quantity":"1","seatCategory":"ELITE"}""")).asJSON
        .check(status.is(200))
      )
      .pause(500 milliseconds)
      .exitHereIfFailed
      .exec(http("book")
        .post("/chennai/ticket/${movie_name}/book")
        .param("""sessionId""", """${session_id}""")
        .param("""seatCategory""", """ELITE""")
        .param("""quantity""", """1""")
        .param("""cityName""","""chennai""")
        .param("""selectedCity""","""chennai""")
        .check(status.is(200), jsonPath("$.orderId").exists.saveAs("orderId"))
      )
      .exitHereIfFailed
      .exec(http("layout")
       .post("/screen/layout")
       .headers(headerAjaxJson)
       .body(StringBody("""{"sessionId":"${session_id}","quantity":"1","seatCategory":"ELITE","orderId":"${orderId}","isAutoSelected":false}""")).asJSON
       .check(status.is(200)))
       .pause(2 seconds)
      .exitHereIfFailed
      .exec(http("orderDetail")
        .get("/order/details")
        .headers(hearderForJsonGet)
        .queryParam("""sessionId""","""${session_id}""")
        .queryParam("""quantity""","""1""")
        .queryParam("""seatCategory""","""ELITE""")
        .check(status.is(200)))
       .pause(500 milliseconds)
      .exitHereIfFailed
      .exec(http("auto select")
        .post("/chennai/ticket/${movie_name}/auto-select")
        .headers(headerAjaxJson)
        .body(StringBody("""{"sessionId":"${session_id}","quantity":"1","seatCategory":"ELITE","movieName":"${movie_name}","isAutoSelected":true}""")).asJSON
        .check(status.is(200))
      )
      .exitHereIfFailed
      .pause(2 seconds)
      .exec(http("get payment options")
        .get("/payment/options")
        .check(status.is(200))
      )
      .pause(500 milliseconds)
      .exitHereIfFailed
      .exec(http("start pay")
        .post("/payment/juspay")
        .headers(headerAjaxJson)
        .body(StringBody("""{"orderId":"${orderId}"}""")).asJSON
        .check(status.is(200)))
      .exitHereIfFailed
      .pause(1 seconds)
      .exec(http("confirm fuel pay")
      .post("/fuel")
      .param("""fuelCardNumber""","9000000000000000")
      .param("""pin""","1111")
      .param("""orderId""","${orderId}")
      .param("""tc""","true")
      .check(status.is(303)))
  setUp(scn.inject(ramp(5000 users) over (200 seconds))).protocols(httpConf)
}