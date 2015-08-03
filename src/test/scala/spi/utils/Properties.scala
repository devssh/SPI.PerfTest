package spi.utils

object Properties{
  val baseUrl = sys.env.getOrElse("SPICINEMAS_URL", "http://192.168.57.115")
}
