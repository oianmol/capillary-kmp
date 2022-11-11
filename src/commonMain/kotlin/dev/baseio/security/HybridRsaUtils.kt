package dev.baseio.security

import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.spec.OAEPParameterSpec

expect object HybridRsaUtils {

  actual fun encrypt(
    plaintext: ByteArray?,
    publicKey: PublicKey?,
    padding: RsaEcdsaConstants.Padding,
    oaepParams: OAEPParameterSpec?
  ): ByteArray

  actual fun decrypt(
    ciphertext: ByteArray,
    privateKey: PrivateKey,
    padding: RsaEcdsaConstants.Padding,
    oaepParams: OAEPParameterSpec
  ): ByteArray

}