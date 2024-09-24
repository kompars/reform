package org.kompars.reform.kotlinx.datetime.validation

import kotlinx.datetime.*
import org.kompars.reform.*
import org.kompars.reform.validation.*

public data object InstantInPast : ValidationError("instant_in_past")

public fun <T : Instant?> SingleField<T>.inFuture(): SingleField<T> {
    return addValidator { value ->
        when {
            value <= Clock.System.now() -> InstantInPast
            else -> null
        }
    }
}

public data object InstantInFuture : ValidationError("instant_in_future")

public fun <T : Instant?> SingleField<T>.inPast(): SingleField<T> {
    return addValidator { value ->
        when {
            value >= Clock.System.now() -> InstantInFuture
            else -> null
        }
    }
}
