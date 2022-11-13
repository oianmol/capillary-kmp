package dev.baseio.security

import java.security.spec.MGF1ParameterSpec
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

actual class OAEPParameterSpec(
  mdName: String = "SHA-256",
  mgfName: String = "MGF1",
  mgf1ParameterSpec: MGF1ParameterSpec = MGF1ParameterSpec.SHA1,
  pSrc: PSource.PSpecified = PSource.PSpecified.DEFAULT
) {
  var oaepParamSpec: OAEPParameterSpec =
    OAEPParameterSpec(mdName, mgfName, mgf1ParameterSpec, pSrc)
}