
package spi.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.joda.time.DateTime


object Payment {

  var paymentInitiate = http("payment_initiate")
    .post("/payment/initiate")
    .header("Authorization","Bearer ${authToken}")
    .body(StringBody("""{"source": "WWW","amount": 430,"paymentType": "juspay","orderId":"${orderId}"}""")).asJSON
    .check(status.is(200),jsonPath("$.paymentReferenceNumber").exists.saveAs("paymentReferenceNumber"))

  var paymentOptions =  http("payment_options")
    .get("/payment/options")
    .header("Authorization","Bearer ${authToken}")
    .queryParam("paymentRequestId","${paymentRequestId}")
    .check(status.is(200))

  var citrusBank   = http("citrus_bank")
    .get("/citrus/banks")
    .check(status.is(200))


  var paymentBannyan   = http("bannyan")
    .post("/payment/banyanAndTC")
    .body(StringBody("""{"banyan": true,"tc": true,"orderId": "${paymentReferenceNumber}"}""")).asJSON
    .check(status.is(200))

  var payThroughWallet  =  http("wallet_pay")
    .post("/payment/wallet")
    .body(StringBody("""entityId=${orderId}&paymentRequestId=${paymentRequestId}&tc=true&banyan=true""")).asFormUrlEncoded
    .check(status.is(303))


  var payJustPay = http("pay_justpay").post("/payment/juspay").body(StringBody("""{"orderId": "${orderId}"}""")).asJSON.check(status.is(200))


}
