package spi.utils

object Properties{
  val baseUrl = sys.env.getOrElse("SPICINEMAS_URL", "https://devtest.spicinemas.in")
  val walletUrl = sys.env.getOrElse("wallet_URL", "http://192.168.57.126:7070")
}
