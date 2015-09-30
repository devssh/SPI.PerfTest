package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.DataSetup._
import spi.endpoints.Cinemas._
import spi.endpoints.Oauth._
import spi.endpoints.Wallet._
import spi.utils.Properties._

import scala.concurrent.duration._

class SimulateError  extends Simulation  {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
    List( extraInfo.request,extraInfo.request.getUri ,extraInfo.session,extraInfo.request.getHeaders,extraInfo.request.getCookies,
      extraInfo.request.getFormParams,extraInfo.request.getQueryParams,
      extraInfo.response.headers,extraInfo.response.cookies,extraInfo.response.statusCode,extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect

  val walletPayRecharge = scenario("Wallet_Pay_Recharge").feed(walletFeeder)
    .exec(walletPay)
    .exec(walletRecharge)

  val walletTransaction = scenario("wallet_transaction").feed(userFeeder).feed(movieFeeder).feed(quantityFeeder)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .repeat(100)
    {
      exec(walletDetails)
    .exec(walletTransactions)
    }


  val walletSearch = scenario("wallet_search").feed(oldFuelFeeder).feed(userFeeder).feed(walletFeeder)
    .exec(walletById)
    .exec(walletByEmail)



  setUp(
    walletTransaction.inject(constantUsersPerSec(100) during(30 second))
  ).protocols(httpConf)
}
