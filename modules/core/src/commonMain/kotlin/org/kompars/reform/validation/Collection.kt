package org.kompars.reform.validation

import org.kompars.reform.*

public data class TooLittleItems(val minSize: Int) :
    ValidationError("too_little_items", "minSize" to minSize)

public fun <T> CollectionField<T>.minSize(minSize: Int): CollectionField<T> {
    return addValidator { value ->
        when {
            value.size < minSize -> TooLittleItems(minSize)
            else -> null
        }
    }
}

public data class TooManyItems(val maxSize: Int) :
    ValidationError("too_many_items", "maxSize" to maxSize)

public fun <T> CollectionField<T>.maxSize(maxSize: Int): CollectionField<T> {
    return addValidator { value ->
        when {
            value.size > maxSize -> TooManyItems(maxSize)
            else -> null
        }
    }
}

public data class InvalidItemCount(val size: Int) :
    ValidationError("invalid_item_count", "size" to size)

public fun <T> CollectionField<T>.exactSize(size: Int): CollectionField<T> {
    return addValidator { value ->
        when {
            value.size != size -> InvalidItemCount(size)
            else -> null
        }
    }
}

public data object DuplicateItems : ValidationError("duplicate_items")

public fun <T> CollectionField<T>.distinct(block: (T) -> Any? = { it }): CollectionField<T> {
    return addValidator { value ->
        val hashset = hashSetOf<Any?>()
        val unique = value.all { hashset.add(block(it)) }

        when (unique) {
            false -> DuplicateItems
            else -> null
        }
    }
}

public data object NotExactlyOne : ValidationError("not_exactly_one")

public fun <T> CollectionField<T>.exactOne(block: (T) -> Boolean): CollectionField<T> {
    return addValidator { value ->
        when (value.filter { block(it) }.size) {
            1 -> null
            else -> NotExactlyOne
        }
    }
}

public data object MoreThanOne : ValidationError("more_than_one")

public fun <T> CollectionField<T>.atMostOne(block: (T) -> Boolean): CollectionField<T> {
    return addValidator { value ->
        when (value.filter { block(it) }.size) {
            0, 1 -> null
            else -> MoreThanOne
        }
    }
}
