
package spi

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.core.validation.Validation
import io.gatling.jdbc.Predef._
import com.redis._
import io.gatling.redis.feeder.RedisFeeder


object DataSetup {
  val databaseUrl: String = "192.168.57.98:5432";

  val sessionsQuery = "select movie_name as full_movie_name,slugged_movie_name as movie_name, session_id,cinema_name,date(start_time) as date,category, food_items.id as food_id " +
    "from session_category_prices " +
    "join sessions on session_id = sessions.id " +
    "JOIN cinemas ON lower(cinemas.name)=lower(sessions.cinema_name) " +
    "JOIN food_location_mapping ON food_location_mapping.location_id=cinemas.id AND food_location_mapping.is_active=true " +
    "JOIN food_groups on food_groups.id=food_location_mapping.group_id AND food_groups.is_active=true " +
    "JOIN food_items ON food_items.group_id=food_groups.id  AND food_items.is_active=true " +
    "where start_time>CURRENT_DATE+3  and start_time<=CURRENT_DATE+7 and sessions.cinema_name != 'thecinema@BROOKEFIELDS'  and food_items.is_active='t' " +
    "limit 500"
  val usersQuery = "select distinct(email) from users where is_active is true and password='yd+8vQnf2ajO3RZxAecJXw==' limit 10000"

  private val cinemasDbUrl: String = "jdbc:postgresql://" + databaseUrl + "/spi_cinemas"
  private val authDbUrl: String = "jdbc:postgresql://" + databaseUrl + "/spi_auth"
  private val userName: String = "postgres"

  val movieFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(cinemasDbUrl, userName,"",sessionsQuery).circular
  val userFeeder: RecordSeqFeederBuilder[Any] = jdbcFeeder(authDbUrl, userName,"",usersQuery).circular
  val quantityFeeder = csv("quantity.csv").random

  var authTokensList: List[Map[String,String]] = List[Map[String,String]]();

  var storeAuthTokenToRedis: (Session) => Validation[Session] = session => {
    val sessionAuthToken: String = session("authToken").as[String]

    authTokensList = authTokensList:+ Map("authToken"-> sessionAuthToken)

    session
  }
  var authTokenFeeder = authTokensList.toArray.circular



}