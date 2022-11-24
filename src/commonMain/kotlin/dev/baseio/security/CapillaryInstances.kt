package dev.baseio.security

object CapillaryInstances {
  private val instances = hashMapOf<String, Capillary>()
  private val lock = Any()
  fun getInstance(chainId: String,isTest:Boolean = false): Capillary {
    if (instances.containsKey(chainId)) {
      return instances[chainId]!!
    }
    // TODO make this synchronized ?
    return Capillary(chainId).also {
      it.initialize(isTest = isTest)
      instances[chainId] = it
    }
  }
}