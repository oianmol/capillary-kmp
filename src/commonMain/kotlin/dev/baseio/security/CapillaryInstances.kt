package dev.baseio.security

object CapillaryInstances {
  private val instances = hashMapOf<String, Capillary>()
  private val lock = Any()
  fun getInstance(chainId: String): Capillary {
    if (instances.containsKey(chainId)) {
      return instances[chainId]!!
    }
    // TODO make this synchronized ?
    return Capillary(chainId).also {
      it.initialize()
      instances[chainId] = it
    }
  }
}