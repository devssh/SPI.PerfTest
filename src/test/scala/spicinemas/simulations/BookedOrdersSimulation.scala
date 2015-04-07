package spicinemas.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

import spicinemas.utils.Properties._

class BookedOrdersSimulation extends Simulation {
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

    val bookedOrderFeeder = csv("orders_booked.csv").random    
    val scn = scenario("quick book")        
    .exec(http("account-logged")
        .get("/account/logged")
        .headers(headerAjax)
        .check(status.is(401))
    )
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
    .feed(bookedOrderFeeder)
    .exec(http("authenticate")
        .post("/account/authenticate")
        .headers(headerAjaxForm)
        .param("user", """${username}""")
        .param("password", """twpass""")
    .check(status.is(200)))    
    .exec(http("get sessions")
      .get("/chennai/sessions")                    
      .check(status.is(200))
    )    
    .exec(http("post sessions")
      .post("/chennai/sessions")
      .headers(headerAjaxJson)
        .body(StringBody("""{"cinema":"${cinema_name}","date":"""" + s"${currentDate}" + """","movie":"${movie_name}"}""")).asJSON
      .check(status.is(200))
    )
    .pause(500 milliseconds)        
    .exec(http("order status")
      .post("/order/status")
      .headers(headerAjaxJson)
      .body(StringBody("""{"sessionId":"${session_id}","quantity":"1","seatCategory":"ELITE"}""")).asJSON
      .check(status.is(200))
    )
    .exec(http("orderDetail")
      .get("/order/details/${orderid}")      
      .headers(hearderForJsonGet)              
      .queryParam("""sessionId""","""${session_id}""")
      .queryParam("""quantity""","""1""")
      .queryParam("""seatCategory""","""ELITE""")            
      .check(status.is(200)))
    .exec(http("get food")
      .post("/food")      
      .headers(headerAjaxJson)              
      .body(StringBody("""$cinema_name""")).asJSON
      .check(status.is(200)))        
    // .exec(http("buy food")
    //   .post("/food/buy")
    //   .headers(headerAjaxJson)      
    //   .body(StringBody("""{"orderId":"$orderId","foodWithQty":{"578":1,"623":1}}""")).asJSON
    //   .check(status.is(200))
    // )
    .exec(http("get payment options")
      .get("/payment/options")
      .check(status.is(200))
    )
    // .pause(300 milliseconds)
    // .exec(http("cancel ticket")
    //   .post("/chennai/ticket/cancel")
    //   .headers(headerAjaxJson)
    //   .body(StringBody("""{"sessionId":"${session_id}","selectedCity":"chennai","orderId":${orderId},"seatCategory":"ELITE","quantity":"1"}""")).asJSON          
    //   .check(status.is(200))
    // )    
  setUp(scn.inject(ramp(1 users) over (1 seconds))).protocols(httpConf)
}