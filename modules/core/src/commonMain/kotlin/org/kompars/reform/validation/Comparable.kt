//package org.kompars.reform.validation
//
//import org.kompars.reform.*
//
//public data class NotInCollection<T>(val collection: Iterable<T>) :
//    ValidationError("not_in_collection", "collection" to collection)
//
//public fun <T : Any?, Self : Validatable<T, Self>> Self.inCollection(block: () -> Iterable<T>?): Self {
//    return validateItem { value ->
//        block()?.let { collection ->
//            when (value) {
//                in collection -> null
//                else -> NotInCollection(collection)
//            }
//        }
//    }
//}
//
//public data class NotEqual<T>(val other: T) :
//    ValidationError("not_equal", "other" to other)
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.equalTo(block: () -> T?): Self {
//    return validateItem { value ->
//        block()?.let { other ->
//            when (value.compareTo(other)) {
//                0 -> null
//                else -> NotEqual(other)
//            }
//        }
//    }
//}
//
//public data class GreaterThanOrEqual<T>(val other: T) :
//    ValidationError("greater_than_or_equal", "other" to other)
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.lessThan(block: () -> T?): Self {
//    return validateItem { value ->
//        block()?.let { other ->
//            when {
//                value >= other -> GreaterThanOrEqual(other)
//                else -> null
//            }
//        }
//    }
//}
//
//public data class GreaterThan<T>(val other: T) :
//    ValidationError("greater_than", "other" to other)
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.lessOrEqualThan(block: () -> T?): Self {
//    return validateItem { value ->
//        block()?.let { other ->
//            when {
//                value > other -> GreaterThan(other)
//                else -> null
//            }
//        }
//    }
//}
//
//public data class LessThanOrEqual<T>(val other: T) :
//    ValidationError("less_than_or_equal", "other" to other)
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.greaterThan(block: () -> T?): Self {
//    return validateItem { value ->
//        block()?.let { other ->
//            when {
//                value <= other -> LessThanOrEqual(other)
//                else -> null
//            }
//        }
//    }
//}
//
//public data class LessThan<T>(val other: T) :
//    ValidationError("less_than", "other" to other)
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.greaterOrEqualThan(block: () -> T?): Self {
//    return validateItem { value ->
//        block()?.let { other ->
//            when {
//                value < other -> LessThan(other)
//                else -> null
//            }
//        }
//    }
//}
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.max(value: T?): Self {
//    return lessOrEqualThan { value }
//}
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.min(value: T?): Self {
//    return greaterOrEqualThan { value }
//}
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.between(range: ClosedRange<T & Any>): Self {
//    return greaterOrEqualThan { range.start }.lessOrEqualThan { range.endInclusive }
//}
//
//public fun <T : Comparable<T & Any>?, Self : Validatable<T, Self>> Self.between(range: OpenEndRange<T & Any>): Self {
//    return greaterOrEqualThan { range.start }.lessThan { range.endExclusive }
//}
