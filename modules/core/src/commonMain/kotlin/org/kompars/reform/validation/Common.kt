package org.kompars.reform.validation

import org.kompars.reform.*

//public fun <T> SingleField<T>.ensure(
//    message: String = "invalid_value",
//    vararg params: Pair<String, Any?>,
//    block: (T & Any) -> Boolean?,
//): SingleField<T> {
//    return validateItem { value ->
//        block(value)?.let { valid ->
//            when (valid) {
//                false -> ValidationError(message, params.toMap())
//                else -> null
//            }
//        }
//    }
//}
//
//public fun <T> CollectionField<T>.ensureAll(
//    message: String = "invalid_value",
//    vararg params: Pair<String, Any?>,
//    block: (List<T>) -> Boolean?,
//): CollectionField<T> {
//    return validateAll { value ->
//        block(value)?.let { valid ->
//            when (valid) {
//                false -> ValidationError(message, params.toMap())
//                else -> null
//            }
//        }
//    }
//}
