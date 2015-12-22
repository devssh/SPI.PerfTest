
package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._


object Wallet {

  var walletPay = http("wallet_pay")
    .post("/wallet/pay")
    .header("Authorization", "Bearer 9TFW9rjG7EXJ09HnQ92SWu1wMyXWAeo4ttDmp6PCqF7sTz4hJPUrVg3Uk3jfAfE0")
    .body(StringBody(
    """ {
          "walletId":"${wallet_id}",
          "userId": "${user_id}",
          "amount":1,
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
    .header("Authorization", "Bearer lOcbvnhCd5FYaWeK04DJeufjBaHxIoO7gJTotdn6EGLnQgKtyCQQ5oBTTm6CPTyb")
    .body(StringBody(
    """ {
          "walletId":"${wallet_id}",
          "rechargeAmount":10000,
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


  var walletById = http("wallet_by_id")
    .post("/wallet/lookup_by_wallet_id")
    .header("Authorization", "Bearer lOcbvnhCd5FYaWeK04DJeufjBaHxIoO7gJTotdn6EGLnQgKtyCQQ5oBTTm6CPTyb")
    .body(StringBody(
    """ {
          "walletId":"${wallet_id}",
          "email":"",
          "mobileNumber":"",
          "oldFuelCardNumber":""
        }""")).asJSON.check(status.is(200))

  var walletDetails = http("wallet_details")
    .get("/wallet/details").check(status.is(200))

  var walletByEmail = http("wallet_by_email")
    .post("/wallet/lookup_by_user_details")
    .header("Authorization", "Bearer lOcbvnhCd5FYaWeK04DJeufjBaHxIoO7gJTotdn6EGLnQgKtyCQQ5oBTTm6CPTyb")
    .body(StringBody(
    """ {"email":"${email}","mobileNumber":"${mobile_number}"}""")).asJSON.check(status.is(200))

  var walletByFuel = http("wallet_by_fuel")
    .post("/wallet/lookup_by_fuel_number")
    .header("Authorization", "Bearer lOcbvnhCd5FYaWeK04DJeufjBaHxIoO7gJTotdn6EGLnQgKtyCQQ5oBTTm6CPTyb")
    .body(StringBody("""{"oldFuelCardNumber":"${fuel_card_number}"}""")).asJSON.check(status.is(200))

  var walletTransactions = http("wallet_transactions")
    .get("/wallet/transaction_history?page=0")
    .check(status.is(200))

}
