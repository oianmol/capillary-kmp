package dev.baseio.protoextensions

import capillary.kmp.*

expect fun KMSlackPublicKey.toByteArray(): ByteArray
expect fun KMHybridRsaCiphertext.toByteArray(): ByteArray
expect fun KMWrappedRsaEcdsaPublicKey.toByteArray(): ByteArray

expect fun ByteArray.toSlackCipherText(): KMSlackCiphertext
expect fun ByteArray.toKMHybridRsaCiphertext(): KMHybridRsaCiphertext
