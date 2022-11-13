package dev.baseio.security


actual fun ByteArray.toPrivateKey(): PrivateKey {
/*  val spec = PKCS8EncodedKeySpec(this)
  val kf = KeyFactory.getInstance("RSA")
  return PrivateKey(kf.generatePrivate(spec))*/
  TODO("")
}

actual fun ByteArray.toPublicKey(): PublicKey {
/*  return PublicKey(KeyFactory.getInstance("RSA").generatePublic(
    X509EncodedKeySpec(this)
  ))*/
  TODO("")
}