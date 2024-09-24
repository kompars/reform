package org.kompars.reform.validation

import org.kompars.reform.*

public data class NotInCollection<T>(val collection: Iterable<T>) :
    ValidationError("not_in_collection", "collection" to collection)

public fun <T : Any?> SingleField<T>.inCollection(block: () -> Iterable<T>?): SingleField<T> {
    return addValidator { value ->
        block()?.let { collection ->
            when (value) {
                in collection -> null
                else -> NotInCollection(collection)
            }
        }
    }
}

public data class NotEqual<T>(val other: T) :
    ValidationError("not_equal", "other" to other)

public fun <T : Comparable<T & Any>?> SingleField<T>.equalTo(block: () -> T?): SingleField<T> {
    return addValidator { value ->
        block()?.let { other ->
            when (value.compareTo(other)) {
                0 -> null
                else -> NotEqual(other)
            }
        }
    }
}

public data class GreaterThanOrEqual<T>(val other: T) :
    ValidationError("greater_than_or_equal", "other" to other)

public fun <T : Comparable<T & Any>?> SingleField<T>.lessThan(block: () -> T?): SingleField<T> {
    return addValidator { value ->
        block()?.let { other ->
            when {
                value >= other -> GreaterThanOrEqual(other)
                else -> null
            }
        }
    }
}

public data class GreaterThan<T>(val other: T) :
    ValidationError("greater_than", "other" to other)

public fun <T : Comparable<T & Any>?> SingleField<T>.lessOrEqualThan(block: () -> T?): SingleField<T> {
    return addValidator { value ->
        block()?.let { other ->
            when {
                value > other -> GreaterThan(other)
                else -> null
            }
        }
    }
}

public data class LessThanOrEqual<T>(val other: T) :
    ValidationError("less_than_or_equal", "other" to other)

public fun <T : Comparable<T & Any>?> SingleField<T>.greaterThan(block: () -> T?): SingleField<T> {
    return addValidator { value ->
        block()?.let { other ->
            when {
                value <= other -> LessThanOrEqual(other)
                else -> null
            }
        }
    }
}

public data class LessThan<T>(val other: T) :
    ValidationError("less_than", "other" to other)

public fun <T : Comparable<T & Any>?> SingleField<T>.greaterOrEqualThan(block: () -> T?): SingleField<T> {
    return addValidator { value ->
        block()?.let { other ->
            when {
                value < other -> LessThan(other)
                else -> null
            }
        }
    }
}

public fun <T : Comparable<T & Any>?> SingleField<T>.max(value: T?): SingleField<T> {
    return lessOrEqualThan { value }
}

public fun <T : Comparable<T & Any>?> SingleField<T>.min(value: T?): SingleField<T> {
    return greaterOrEqualThan { value }
}

public fun <T : Comparable<T & Any>?> SingleField<T>.between(range: ClosedRange<T & Any>): SingleField<T> {
    return greaterOrEqualThan { range.start }.lessOrEqualThan { range.endInclusive }
}

public fun <T : Comparable<T & Any>?> SingleField<T>.between(range: OpenEndRange<T & Any>): SingleField<T> {
    return greaterOrEqualThan { range.start }.lessThan { range.endExclusive }
}
