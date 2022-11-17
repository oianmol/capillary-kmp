package dev.baseio.security

expect object Capillary {
  fun initialize()
  fun getPublicKeyString(): ByteArray?
}