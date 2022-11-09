package dev.baseio.protoextensions

import dev.baseio.slackdata.securepush.*

expect fun KMSlackPublicKey.toByteArray(): ByteArray
expect fun KMHybridRsaCiphertext.toByteArray(): ByteArray
expect fun KMWrappedRsaEcdsaPublicKey.toByteArray(): ByteArray
expect fun KMSecureNotification.toByteArray(): ByteArray

expect fun ByteArray.toSecureNotification(): KMSecureNotification
expect fun ByteArray.toSlackCipherText(): KMSlackCiphertext
expect fun ByteArray.toKMHybridRsaCiphertext(): KMHybridRsaCiphertext
