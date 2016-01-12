package spi.utils

object Properties{
  val baseUrl = sys.env.getOrElse("SPICINEMAS_URL", "https://devtest.spicinemas.in")
  val localUrl = sys.env.getOrElse("SPICINEMAS_URL", "http://127.0.0.1")
  val internelNginx = sys.env.getOrElse("SPICINEMAS_URL", "http://192.168.57.114:3000")
  val walletUrl = sys.env.getOrElse("wallet_URL", "http://192.168.57.126:7070")
}
