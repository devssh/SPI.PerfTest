package spicinemas.utils

object Properties{
  val baseUrl = sys.env.getOrElse("SPICINEMAS_URL", "http://devtest.spicinemas.in")
}
