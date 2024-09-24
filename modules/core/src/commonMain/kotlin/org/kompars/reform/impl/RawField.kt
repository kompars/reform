package org.kompars.reform.impl

import org.kompars.reform.*
import org.kompars.reform.validation.*

internal class RawFieldImpl<T : Any>(
    private val form: FormImpl,
    private val converter: (String?) -> T?,
) : SingleField<T?>, RawField<T> {
    override fun <U : Any> convert(block: (T) -> U): RawField<U> {
        return RawFieldImpl(
            form = form,
            converter = { converter(it)?.let(block) },
        )
    }

    override fun required(block: () -> Boolean): OptionalField<T> {
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

    override fun default(block: () -> T): RequiredField<T> {
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

    override fun addValidator(block: (T) -> ValidationError?): OptionalField<T> {
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
