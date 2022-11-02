package dev.baseio.capillary

/**
 * Creates a new [CapillaryException] instance.
 * Base checked exception class for Capillary-related errors.
 * @param msg the exception message.
 */
open class CapillaryException(msg: String?) : Throwable(msg)