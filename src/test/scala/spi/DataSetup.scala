
package spi

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.core.validation.Validation
import io.gatling.jdbc.Predef._


object DataSetup {
//  val databaseUrl: String = "127.0.0.1:5432";
  val databaseUrl: String = "192.168.57.98:5432";

  val sessionsQuery =
    "select movie_name as full_movie_name,slugged_movie_name as movie_name,session_id,cinema_name,date(start_time) as date,category " +
    "from session_category_prices join sessions on session_id = sessions.id  where   " +
    "sessions.id in (select id from sessions where status = 'ACTIVE' and date(start_time) in (CURRENT_DATE+5) and cinema_name = 'Sathyam Cinemas' order by start_time desc limit 20) or " +
    "sessions.id in (select id from sessions where status = 'ACTIVE' and date(start_time) in (CURRENT_DATE+5) and cinema_name = 'Escape' order by start_time desc  limit 20) "
//  Sathyam Cinemas
//  Escape
//  SPI Cinemas Palazzo
//  S2 PERAMBUR


  val usersQuery = "select email,mobile_number from users " +
    "where is_active is true and password='yd+8vQnf2ajO3RZxAecJXw==' " +
    " order by created_at limit 7000"

  val walletQuery = "select wallet_id,user_id from wallets " +
    "where is_active is true " +
    "limit 7000"

  val oldFuelQuery = "select fuel_card_number from old_fuel_details"


  private val cinemasDbUrl: String = "jdbc:postgresql://" + databaseUrl + "/spi_cinemas"
//  private val authDbUrl: String = "jdbc:postgresql://" + databaseUrl + "/auth_dev"
  private val authDbUrl: String = "jdbc:postgresql://" + databaseUrl + "/spi_auth"
  private val walletDbUrl: String = "jdbc:postgresql://" + databaseUrl + "/spi_wallet"
  private val userName: String = "postgres"


  val movieFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(cinemasDbUrl, userName,"",sessionsQuery).circular
  val userFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(authDbUrl, userName,"",usersQuery).circular
  val walletFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(walletDbUrl, userName,"",walletQuery).circular
  val oldFuelFeeder =  csv("quantity.csv").random
  val quantityFeeder = csv("quantity.csv").random
  val preBookFeeder = csv("prebook.csv").circular
  var authTokensList: List[Map[String,String]] = List[Map[String,String]]()

  var storeAuthTokenToRedis: (Session) => Validation[Session] = session => {
    val sessionAuthToken: String = session("authToken").as[String]

    authTokensList = authTokensList:+ Map("authToken"-> sessionAuthToken)

    session
  }
  var authTokenFeeder = authTokensList.toArray.circular

}