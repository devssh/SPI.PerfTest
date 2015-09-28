package spi.utils

object Properties{
  val baseUrl = sys.env.getOrElse("SPICINEMAS_URL", "https://devtest.spicinemas.in")
}
