package spi.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import spi.DataSetup._
import spi.endpoints.Wallet._
import spi.utils.Properties._

import scala.concurrent.duration._

class Wallet  extends Simulation  {

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


  setUp(
    walletPayRecharge.inject(
      constantUsersPerSec(100) during(5 minute))
  ).protocols(httpConf)
}
