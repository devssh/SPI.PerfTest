package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.DataSetup._
import spi.endpoints.Oauth._
import spi.endpoints.Wallet._
import spi.utils.Properties._

import scala.concurrent.duration._

class WalletSearch  extends Simulation  {

  val httpConf = http
    .baseURL(baseUrl)
    .extraInfoExtractor(extraInfo =>
    List( extraInfo.request,extraInfo.request.getUri ,extraInfo.session,extraInfo.request.getHeaders,extraInfo.request.getCookies,
      extraInfo.request.getFormParams,extraInfo.request.getQueryParams,
      extraInfo.response.headers,extraInfo.response.cookies,extraInfo.response.statusCode,extraInfo.response.body))
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:37.0) Gecko/20100101 Firefox/37.0")
    .disableFollowRedirect

  val walletTransaction = scenario("wallet_transaction").feed(userFeeder)
    .exec(userAuthentication)
    .exec(getAuthorizationToken)
    .exec(walletTransactions)

  val walletSearch = scenario("wallet_search").feed(oldFuelFeeder).feed(userFeeder).feed(walletFeeder)
    .exec(walletById)
    .exec(walletByEmail)
    .exec(walletByFuel)



  setUp(
    walletSearch.inject(constantUsersPerSec(1) during(1 second)),
    walletTransaction.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
