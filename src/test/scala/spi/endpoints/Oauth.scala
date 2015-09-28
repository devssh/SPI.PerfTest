
package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.DateTime


object Oauth {

   var loginPage  = http("login_page")
     .get("/oauth2/login")
     .check(status.is(200))

   var userAuthentication  = http("user authentication")
     .post("/oauth2/login")
     .body(StringBody("username=${email}&password=!abcd1234")).asFormUrlEncoded
     .check(status.in(List(302)))

   var userInfo = http("user info")
     .get("/oauth2/user/info")
     .header("Authorization","Bearer ${authToken}")
     .check(status.in(List(304,200)))

  var totpRequest = http("totp id")
    .get("/oauth2/transaction_id?delivery_mode=online")
    .header("Authorization","Bearer ${authToken}")
//    .body(StringBody("""delivery_mode:"online""")).asFormUrlEncoded
    .check(status.in(List(200)))

   var loggedUserCheck = http("account-logged")
     .get("/oauth2/authorize")
     .queryParam("client_id","spi-web")
     .queryParam("redirect_uri","https://devtest.spicinemas.in/user/profile")
     .queryParam("state","spi_start")
     .queryParam("response_type","spi_token")
     .check(status.in(List(302)))


   var getAuthorizationToken = http("get authentication token")
     .get("/oauth2/authorize")
     .queryParam("client_id","spi-web")
     .queryParam("redirect_uri","https://devtest.spicinemas.in/user/profile")
     .queryParam("state","spi_start")
     .queryParam("response_type","spi_token")
     .check(status.in(List(200)), jsonPath("$.success").exists.saveAs("authToken"))


 }
