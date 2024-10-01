package org.kompars.reform

import kotlin.reflect.*
import org.kompars.reform.validation.*

public interface Form {
    public val attributes: Map<String, Any>
    public val values: Map<String, List<Any?>>
    public val errors: List<ValidationError>
    public val fields: List<Field<*>>

    public suspend fun field(name: String): SingleField<String?>
    public suspend fun collection(name: String): CollectionField<String?>

    public suspend fun validate(block: suspend () -> ValidationError?)
    public fun addError(error: ValidationError)
}

public class FormImpl(
    public override val values: Map<String, List<Any?>>,
) : Form {
    public override val attributes: MutableMap<String, Any> = mutableMapOf()
    public override val errors: MutableList<ValidationError> = mutableListOf()
    public override val fields: MutableList<Field<*>> = mutableListOf()

    public override suspend fun field(name: String): SingleField<String?> {
        TODO()
    }

    public override suspend fun collection(name: String): CollectionField<String?> {
        TODO()
    }

    public override suspend fun validate(block: suspend () -> ValidationError?) {
        block()?.let { addError(it) }
    }

    public override fun addError(error: ValidationError) {
        errors += error
    }
}

public interface Field<T> {
    public val form: Form
    public val name: String
    public val value: T
}

public operator fun <T> Field<T>.provideDelegate(thisRef: Any?, property: KProperty<*>): Lazy<T> {
    return lazy { value }
}

public interface Validatable<T, Self : Validatable<T, Self>> {
    public suspend fun validateItem(block: suspend (T & Any) -> ValidationError?): Self
}

public interface SingleField<T> : Field<T>, Validatable<T, SingleField<T>> {
    override suspend fun validateItem(block: suspend (T & Any) -> ValidationError?): SingleField<T>
    public fun <U : Any> convert(block: suspend (T & Any) -> U): SingleField<U>

    public fun default(block: suspend () -> T): SingleField<T & Any>
    public fun required(): SingleField<T & Any>
    public fun required(block: suspend () -> Boolean): SingleField<T>
}

public interface CollectionField<T> : Field<List<T>>, Validatable<T, CollectionField<T>> {
    override suspend fun validateItem(block: suspend (T & Any) -> ValidationError?): CollectionField<T>
    public fun validateAll(block: suspend (List<T>) -> ValidationError?): CollectionField<T>
    public fun <U> convertAll(block: suspend (List<T>) -> List<U>): CollectionField<U>
    public fun <U : Any> convert(block: suspend (T & Any) -> U): CollectionField<U>

    public fun default(block: suspend () -> T & Any): CollectionField<T & Any>
    public fun required(): CollectionField<T & Any>
    public fun required(block: suspend () -> Boolean): CollectionField<T>
}


//public fun Form.addError(name: String? = null, message: String, vararg params: Pair<String, Any?>) {
//    addError(name, ValidationError(message, *params))
//}
//
//public fun Form.throwError(name: String? = null, message: String, vararg params: Pair<String, Any?>, cause: Throwable? = null): Nothing {
//    addError(name, message, *params)
//    throw InvalidFormException(this, cause)
//}
//
//public fun Form.throwError(message: String, vararg params: Pair<String, Any?>, cause: Throwable? = null): Nothing {
//    throwError(name = null, message = message, params = params)
//}
//
//public fun <T> Form.catchError(name: String? = null, message: String, vararg params: Pair<String, Any?>, block: () -> T): T {
//    return try {
//        block()
//    } catch (e: InvalidFormException) {
//        throw e
//    } catch (e: Throwable) {
//        throwError(name, message, *params, cause = e)
//    }
//}
//
//public fun <T> Form.catchError(message: String, vararg params: Pair<String, Any?>, block: () -> T): T {
//    return catchError(name = null, message = message, params = params, block = block)
//}
