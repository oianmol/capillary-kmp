package dev.baseio.security

import java.security.spec.MGF1ParameterSpec
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

actual class OAEPParameterSpec actual constructor() {
  var oaepParamSpec: OAEPParameterSpec =
    OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT)
}