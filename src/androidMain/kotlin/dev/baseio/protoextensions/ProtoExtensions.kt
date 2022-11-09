package dev.baseio.protoextensions

import capillary.kmp.*

actual fun KMSlackPublicKey.toByteArray(): ByteArray {
    val builder = SlackPublicKey.newBuilder()
    builder.addAllKeybytes(keybytesList.map { ByteArrayElement.newBuilder().setByte(it.byte).build() })
    return builder.build().toByteArray()
}

actual fun KMHybridRsaCiphertext.toByteArray(): ByteArray {
    return HybridRsaCiphertext.newBuilder()
        .addAllSymmetrickeyciphertext(this.symmetrickeyciphertextList.map {
            ByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .addAllPayloadciphertext(this.payloadciphertextList.map {
            ByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .build().toByteArray()
}

actual fun KMWrappedRsaEcdsaPublicKey.toByteArray(): ByteArray {
    return WrappedRsaEcdsaPublicKey.newBuilder()
        .setPadding(this.padding)
        .addAllKeybytes(this.keybytesList.map {
            ByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .build().toByteArray()
}

actual fun ByteArray.toSlackCipherText(): KMSlackCiphertext {
    val secureNotification = SlackCiphertext.parseFrom(this)
    return kmSlackCiphertext {
        this.ciphertextList.addAll(secureNotification.ciphertextList.map { it ->
            kmByteArrayElement {
                this.byte = it.byte
            }
        })
    }
}

actual fun ByteArray.toKMHybridRsaCiphertext(): KMHybridRsaCiphertext {
    val secureNotification = HybridRsaCiphertext.parseFrom(this)
    return kmHybridRsaCiphertext {
        symmetrickeyciphertextList.addAll(secureNotification.symmetrickeyciphertextList.map { it ->
            kmByteArrayElement {
                this.byte = it.byte
            }
        })
        payloadciphertextList.addAll(secureNotification.payloadciphertextList.map { it ->
            kmByteArrayElement {
                this.byte = it.byte
            }
        })
    }
}