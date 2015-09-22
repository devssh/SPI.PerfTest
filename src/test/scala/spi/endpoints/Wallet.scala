
package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._


object Wallet {

  var walletPay = http("wallet_pay")
    .post("/wallet/pay")
    .header("Authorization", "Bearer OAv0pAbJTp7GqEKeC5zttDvfCWCXqS4wxx2tzw5mLfO3OBRAXkYH0nuV2S7Ip5WY")
    .body(StringBody(
    """ {
          "walletId":"${wallet_id}",
          "userId": "${user_id}",
          "amount":100,
          "clientIp":"10.10.1.01",
          "orderId":"${wallet_id}",
          "udf1":"1",
          "udf2":"2",
          "udf3":"3",
          "udf4":"4",
          "udf5":"5",
          "udf6":"6",
          "udf7":"7",
          "udf8":"8",
          "udf9":"9",
          "udf10":"10"
        }""")).asJSON.check(status.is(200))

  var walletRecharge = http("wallet_recharge")
    .post("/wallet/credit/recharge")
    .header("Authorization", "Bearer OAv0pAbJTp7GqEKeC5zttDvfCWCXqS4wxx2tzw5mLfO3OBRAXkYH0nuV2S7Ip5WY")
    .body(StringBody(
    """ {
          "walletId":"${wallet_id}",
          "rechargeAmount":100,
          "clientIp":"10.10.1.01",
          "orderId":"123",
          "udf1":"1",
          "udf2":"2",
          "udf3":"3",
          "udf4":"4",
          "udf5":"5",
          "udf6":"6",
          "udf7":"7",
          "udf8":"8",
          "udf9":"9",
          "udf10":"10"
        }""")).asJSON.check(status.is(200))



}
