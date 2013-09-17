package spicinemas.utils

object Properties {
  val baseUrl = sys.env.getOrElse("SPICINEMAS_URL", "http://10.16.3.195")
  val host = sys.env.getOrElse("SPICINEMAS_HOST", "10.16.3.195")
  val databaseUrl = sys.env.getOrElse("databaseUrl", "jdbc:postgresql://" + host + ":5432/spi_cinemas")
  val dbUsername = sys.env.getOrElse("DATABASE_USER", "postgres")
  val dbPassword = sys.env.getOrElse("DATABASE_PASS", "")
}