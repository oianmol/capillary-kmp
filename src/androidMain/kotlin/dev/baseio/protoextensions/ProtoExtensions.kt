package dev.baseio.protoextensions

import dev.baseio.slackdata.securepush.*
import dev.baseio.slackdata.protos.*

actual fun KMWrappedWebPushPublicKey.toByteArray(): ByteArray {
    return WrappedWebPushPublicKey.newBuilder()
        .addAllAuthsecret(this.authsecretList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .addAllKeybytes(this.keybytesList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .build().toByteArray()
}

actual fun KMSlackPublicKey.toByteArray(): ByteArray {
    val builder = SlackPublicKey.newBuilder()
    builder.setKeychainuniqueid(keychainuniqueid)
    builder.setSerialnumber(serialnumber)
    builder.setIsauth(isauth)
    builder.addAllKeybytes(keybytesList.map { SKByteArrayElement.newBuilder().setByte(it.byte).build() })
    return builder.build().toByteArray()
}


actual fun KMWrappedWebPushPrivateKey.toByteArray(): ByteArray {
    return WrappedWebPushPrivateKey.newBuilder()
        .addAllAuthsecret(this.authsecretList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .addAllPrivatekeybytes(this.privatekeybytesList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        }).addAllPublickeybytes(this.publickeybytesList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .build().toByteArray()
}

actual fun KMHybridRsaCiphertext.toByteArray(): ByteArray {
    return HybridRsaCiphertext.newBuilder()
        .addAllSymmetrickeyciphertext(this.symmetrickeyciphertextList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .addAllPayloadciphertext(this.payloadciphertextList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .build().toByteArray()
}

actual fun KMWrappedRsaEcdsaPublicKey.toByteArray(): ByteArray {
    return WrappedRsaEcdsaPublicKey.newBuilder()
        .setPadding(this.padding)
        .addAllKeybytes(this.keybytesList.map {
            SKByteArrayElement.newBuilder()
                .setByte(it.byte).build()
        })
        .build().toByteArray()
}

actual fun KMSecureNotification.toByteArray(): ByteArray {
    return SecureNotification.newBuilder()
        .setId(this.id)
        .setTitle(this.title)
        .setBody(this.body)
        .build()
        .toByteArray()
}

actual fun ByteArray.toKMWrappedWebPushPrivateKey(): KMWrappedWebPushPrivateKey {
    val privateKey = WrappedWebPushPrivateKey.parseFrom(this)
    return kmWrappedWebPushPrivateKey {
        this.authsecretList.addAll(privateKey.authsecretList.map { it ->
            kmSKByteArrayElement {
                this.byte = it.byte
            }
        })
        this.privatekeybytesList.addAll(privateKey.privatekeybytesList.map { it ->
            kmSKByteArrayElement {
                this.byte = it.byte
            }
        })
        this.publickeybytesList.addAll(privateKey.publickeybytesList.map { it ->
            kmSKByteArrayElement {
                this.byte = it.byte
            }
        })
    }
}

actual fun ByteArray.toSecureNotification(): KMSecureNotification {
    val secureNotification = SecureNotification.parseFrom(this)
    return kmSecureNotification {
        this.id = secureNotification.id
        this.title = secureNotification.title
        this.body = secureNotification.body
    }
}

actual fun ByteArray.toSlackCipherText(): KMSlackCiphertext {
    val secureNotification = SlackCiphertext.parseFrom(this)
    return kmSlackCiphertext {
        this.ciphertextList.addAll(secureNotification.ciphertextList.map { it ->
            kmSKByteArrayElement {
                this.byte = it.byte
            }
        })
        this.isauthkey = secureNotification.isauthkey
        this.keychainuniqueid = secureNotification.keychainuniqueid
        this.keyserialnumber = secureNotification.keyserialnumber
    }
}

actual fun ByteArray.toKMHybridRsaCiphertext(): KMHybridRsaCiphertext {
    val secureNotification = HybridRsaCiphertext.parseFrom(this)
    return kmHybridRsaCiphertext {
        symmetrickeyciphertextList.addAll(secureNotification.symmetrickeyciphertextList.map { it ->
            kmSKByteArrayElement {
                this.byte = it.byte
            }
        })
        payloadciphertextList.addAll(secureNotification.payloadciphertextList.map { it ->
            kmSKByteArrayElement {
                this.byte = it.byte
            }
        })
    }
}