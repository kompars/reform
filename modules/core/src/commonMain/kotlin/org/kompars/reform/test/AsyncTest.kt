package org.kompars.reform.test

import kotlin.getValue
import kotlin.jvm.JvmName
import kotlin.reflect.KProperty
import kotlin.reflect.typeOf
import org.kompars.reform.SingleField
import org.kompars.reform.Validatable
import org.kompars.reform.buildFormValues
import org.kompars.reform.validation.ValidationError

private val values = buildFormValues {
    append("password", "ABC")
    append("userId", "1")
    append("userId", "2")
    append("userId", "4")
    append("userId", "6")
}

public fun interface Converter<T : Any, U : Any> {
    public fun convert(value: T): U
}

public fun interface AsyncConverter<T : Any, U : Any> {
    public suspend fun convert(value: T): U
}

public object StringToInt : Converter<String, Int>, ConstraintAware {
    override val constraint: Constraint by lazy {
        Constraint("not_an_integer")
    }

    override fun convert(value: String): Int {
        return value.toInt()
    }
}

public object IntToString : Converter<Int, String> {
    override fun convert(value: Int): String {
        return value.toString()
    }
}

public fun interface Validator<T : Any> {
    public fun validate(value: T): Boolean
}

public fun interface AsyncValidator<T : Any> {
    public suspend fun validate(value: T): Boolean
}

public class Constraint(
    public val message: String,
    public val context: Map<String, Any?>?,
) {
    public constructor(message: String, vararg context: Pair<String, Any?>) : this(message, mapOf(*context))
}

public interface ConstraintAware {
    public val constraint: Constraint
}

public class MinLength(private val minLength: Int) : Validator<String>, ConstraintAware {
    override val constraint: Constraint by lazy {
        Constraint("string_too_short", "minLength" to minLength)
    }

    override fun validate(value: String): Boolean {
        return value.length >= minLength
    }
}

public class EqualTo<T : Comparable<T>>(private val other: T) : Validator<T>, ConstraintAware {
    override val constraint: Constraint by lazy {
        Constraint("not_equal_to", "other" to other)
    }

    override fun validate(value: T): Boolean {
        return value.compareTo(other) == 0
    }
}

public suspend fun <T : Any?, Self : Validatable<T, Self>> Self.validate1(validator: Validator<T & Any>): Self {
    return validateItem { if (validator.validate(it)) null else ValidationError("SHIIIT") }
}

//public fun <T : String?, Self : Validatable<T, Self>> Self.validate1(constraint: Validator<T & Any>): Self {
//    TODO()
//}

//@Suppress("UNCHECKED_CAST")
//public suspend fun <T : String?, Self : Validatable<T, Self>> Self.minLength2(minLength: Int): Self {
//    return validate1(MinLength(minLength) as Validator<T & Any>)
//}

public suspend fun <Self : Validatable<String?, Self>> Self.minLength(minLength: Int): Self {
    return validate1(MinLength(minLength))
}

public fun <T, U> SingleField<T>.convert1(converter: Converter<T & Any, U & Any>): SingleField<U> {
    TODO()
}

public suspend fun <T, U> SingleField<T>.convert1(converter: () -> Converter<T & Any, U & Any>): SingleField<U> {
    TODO()
}

public class Kokos<T>(
    public val name: String,
    public val value: T?,
    public val errors: MutableList<ValidationError> = mutableListOf(),
) : Validatable<T?, Kokos<T>> {
    override suspend fun validateItem(block: suspend (T & Any) -> ValidationError?): Kokos<T> {
        if (value != null) {
            block(value)?.let { errors += it }
        }

        return this
    }

    public fun <U> convert(converter: (T & Any) -> U): Kokos<U> {
        return Kokos(name, value?.let { converter(it) }, errors)
    }

    public fun required(): Kokos<T & Any> {
        if (value == null) {
            errors += ValidationError("REQUIRED")
        }

        @Suppress("UNCHECKED_CAST")
        return this as Kokos<T & Any>
    }

    public fun multiple(): Kokos<T> {
        // bindToForm
        return this
    }

    public fun single(): SingleKokos<T> {
        // bindToForm
        return SingleKokos(name, value, errors)
    }
}

public class SingleKokos<T>(
    public val name: String,
    public val value: T?,
    public val errors: MutableList<ValidationError> = mutableListOf(),
)

public operator fun <T> SingleKokos<T>.provideDelegate(thisRef: Any?, property: KProperty<*>): Lazy<T> {
    return lazy {
        if (errors.isNotEmpty()) {
            error(name + ": " + errors.first().message)
        }

        @Suppress("UNCHECKED_CAST")
        value as T
    }
}

public operator fun <T> Kokos<T>.provideDelegate(thisRef: Any?, property: KProperty<*>): Lazy<List<T>> {
    return lazy {
        if (errors.isNotEmpty()) {
            error(name + ": " + errors.first().message)
        }

        @Suppress("UNCHECKED_CAST")
        listOf(value) as List<T>
    }
}

public inline fun <reified T> isNullable(): Boolean {
    return typeOf<T>().isMarkedNullable
}

public fun field(name: String): Kokos<String?> {
    return Kokos(name, "nullable")
}

@JvmName("reifiedField")
public inline fun <reified T> field(name: String): Kokos<T> {
    val kokos = Kokos<T?>(name, "nullable" as T?)

    @Suppress("UNCHECKED_CAST")
    return when {
        !isNullable<T>() -> kokos.required() as Kokos<T>
        else -> kokos as Kokos<T>
    }
}

//public inline fun <reified T> collection(name: String): Kokos<T> {
//    return Kokos(name, "null" as T, !isNullable<T>())
//}

public suspend fun main() {
    //val form = FormImpl(values)

    val kokos: String by field("kokos")
        .required()
        .minLength(2)
        //.convert { if (false) null else it.length }
        //.required()
        //.convert { it.toString() + "AVCAAA" }
        //.minLength(5)
        //.convert { it.toIntOrNull() }
        //.required()
        //.validate1 { it > 9 }
        .single()

    val moznaKokos: List<String> by field("moznaKokos")
        .required()
        .minLength(6)
        .multiple()

    //val kokosy by collection<String?>("kokosy")
    //    .minLength(6)

    //.singleLine()
    //.required()
    //.single()

    //val kokos2 = field<String>("kokos2", null)
    //    .minLength(6)
    ////.single()
    //
    //val kokos3 = field<String>("kokos3", null)
    //    .minLength(6)
    //.required()
    //.multiple(min = 2, max = 2)

    println(kokos)
    println(moznaKokos)
    //println(kokosy)

    //val password by form.field("password")
    //    .validate1(MinLength(5))
    //    .minLength(5)
    //    .validate1(MinLength(5))
    //    .convert1 { StringToInt }
    //    .convert1 { IntToString }
    //    .validate1(EqualTo("A"))
    //    .required()
    //
    //val password by form.field("password")
    //    .validate1(MinLength(5))
    //    .minLength(5)
    //    .validate1(MinLength(5))
    //    .convert1 { StringToInt }
    //    .convert1 { IntToString }
    //    .validate1(EqualTo("A"))
    //    .required()
    ////.strongPassword()
    //
    //val users by form.collection("userIds")
    //    .convert { "X".repeat(it.toInt()) }
    //    .required()
    //    .minLength(5)
    //
    //try {
    //    println(password)
    //    println(users)
    //} catch (e: InvalidFormException) {
    //    println(e)
    //}
}
