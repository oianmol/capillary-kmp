package dev.baseio.security

expect object Capillary {
  fun initialize()
  fun getPublicKey(): ByteArray?
  fun getPrivateKey(): ByteArray?
  fun getPublicKeyString(): String?
}