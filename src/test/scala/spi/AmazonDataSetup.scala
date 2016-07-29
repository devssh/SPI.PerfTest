
package spi

import io.gatling.commons.validation.Validation
import io.gatling.core.Predef._


object AmazonDataSetup {
  val oldFuelFeeder =  csv("quantity.csv").random;
  val quantityFeeder = csv("quantity.csv").random
  val movieFeeder = csv("sessions.csv").circular
  val userFeeder =  csv("users.csv").circular
  val walletFeeder =  csv("wallet.csv").circular
  var authTokensList: List[Map[String,String]] = List[Map[String,String]]();
  val preBookFeeder = csv("prebook.csv").circular

  var storeAuthTokenToRedis: (Session) => Validation[Session] = session => {
    val sessionAuthToken: String = session("authToken").as[String]

    authTokensList = authTokensList:+ Map("authToken"-> sessionAuthToken)

    session
  }
  var authTokenFeeder = authTokensList.toArray.circular

}