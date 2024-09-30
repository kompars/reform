package org.kompars.reform.impl

import org.kompars.reform.*
import org.kompars.reform.validation.*

internal class RawFieldImpl<T : Any>(
    private val form: FormImpl,
    private val converter: suspend (String?) -> T?,
) : SingleField<T?>, RawField<T> {
    override fun <U : Any> convert(block: suspend (T) -> U): RawField<U> {
        return RawFieldImpl(
            form = form,
            converter = { converter(it)?.let { block(it) } },
        )
    }

    override fun required(block: suspend () -> Boolean): OptionalField<T> {
        return OptionalFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = { if (block()) throw MissingValueException() else null },
        )
    }

    override fun required(): RequiredField<T> {
        return RequiredFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = { throw MissingValueException() },
        )
    }

    override fun default(block: suspend () -> T): RequiredField<T> {
        return RequiredFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = block,
        )
    }

    override fun collection(): CollectionField<T?> {
        return CollectionFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = { null },
        )
    }

    override fun addValidator(block: suspend (T) -> ValidationError?): OptionalField<T> {
        return OptionalFieldImpl(
            form = form,
            converter = converter,
            validators = mutableListOf(block),
        )
    }

    override fun bind(name: String): Lazy<T?> {
        return form.register(name) { rawValues ->
            val rawValue = when (rawValues.size) {
                0, 1 -> rawValues.singleOrNull()
                else -> return@register FieldResult.Errors(null, listOf(SingleValueExpected))
            }

            getResult(rawValue, null, converter, { null }, emptyList())
        }
    }
}
