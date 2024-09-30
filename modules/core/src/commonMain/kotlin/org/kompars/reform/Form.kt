package org.kompars.reform

import org.kompars.reform.validation.*
import kotlin.reflect.*

public interface Form {
    public val name: String?
    public val id: String?

    //public val rawValues: Map<String, List<Any?>>
    public val values: Map<String, Any?>

    public val errors: Map<String?, List<ValidationError>>
    public val isValid: Boolean

    public fun <T : Any> createField(trim: Boolean = true, block: suspend (String) -> T?): RawField<T>
    public suspend fun process(rawValues: Map<String, List<String?>>)

    public fun addValidator(name: String? = null, block: suspend () -> ValidationError?)
    public fun addError(name: String? = null, error: ValidationError)
}

public interface Field<T> {
    public fun bind(name: String): Lazy<T>
}

public operator fun <T> Field<T>.provideDelegate(thisRef: Any?, property: KProperty<*>): Lazy<T> {
    return bind(property.name)
}

public interface SingleField<T> : Field<T> {
    public fun collection(): CollectionField<T>
    public fun addValidator(block: suspend (T & Any) -> ValidationError?): SingleField<T>
}

public interface CollectionField<T> : Field<List<T>> {
    public fun addValidator(block: suspend (List<T>) -> ValidationError?): CollectionField<T>
}

public interface OptionalField<T : Any> : SingleField<T?> {
    public fun required(block: suspend () -> Boolean): OptionalField<T>
    public fun required(): RequiredField<T>
    public fun default(block: suspend () -> T): RequiredField<T>
}

public interface RequiredField<T : Any> : SingleField<T>

public interface RawField<T : Any> : OptionalField<T> {
    public fun <U : Any> convert(block: suspend (T) -> U): RawField<U>
}

public fun <T : Any> RawField<T>.default(value: T): RequiredField<T> {
    return default { value }
}

public fun Form.addError(name: String? = null, message: String, vararg params: Pair<String, Any?>) {
    addError(name, ValidationError(message, *params))
}

public fun Form.throwError(name: String? = null, message: String, vararg params: Pair<String, Any?>, cause: Throwable? = null): Nothing {
    addError(name, message, *params)
    throw InvalidFormException(this, cause)
}

public fun Form.throwError(message: String, vararg params: Pair<String, Any?>, cause: Throwable? = null): Nothing {
    throwError(name = null, message = message, params = params)
}

public fun <T> Form.catchError(name: String? = null, message: String, vararg params: Pair<String, Any?>, block: () -> T): T {
    return try {
        block()
    } catch (e: InvalidFormException) {
        throw e
    } catch (e: Throwable) {
        throwError(name, message, *params, cause = e)
    }
}

public fun <T> Form.catchError(message: String, vararg params: Pair<String, Any?>, block: () -> T): T {
    return catchError(name = null, message = message, params = params, block = block)
}
