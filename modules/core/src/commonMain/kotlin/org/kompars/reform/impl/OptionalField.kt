package org.kompars.reform.impl

import org.kompars.reform.*
import org.kompars.reform.validation.*

internal class OptionalFieldImpl<T : Any>(
    private val form: FormImpl,
    private val converter: (String?) -> T?,
    private val defaultValueProvider: () -> T? = { null },
    private val validators: List<(T) -> ValidationError?> = mutableListOf(),
) : OptionalField<T>, SingleField<T?> {
    override fun required(block: () -> Boolean): OptionalField<T> {
        return OptionalFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = { if (block()) throw MissingValueException() else null },
            validators = validators,
        )
    }

    override fun required(): RequiredField<T> {
        return RequiredFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = { throw MissingValueException() },
            validators = validators,
        )
    }

    override fun default(block: () -> T): RequiredField<T> {
        return RequiredFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = block,
            validators = validators,
        )
    }

    override fun collection(): CollectionField<T?> {
        return CollectionFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = defaultValueProvider,
            itemValidators = validators,
        )
    }

    override fun addValidator(block: (T) -> ValidationError?): OptionalField<T> {
        return OptionalFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = defaultValueProvider,
            validators = validators + block,
        )
    }

    override fun bind(name: String): Lazy<T?> {
        return form.register(name) { rawValues ->
            val rawValue = when (rawValues.size) {
                0, 1 -> rawValues.singleOrNull()
                else -> return@register FieldResult.Errors(null, listOf(SingleValueExpected))
            }

            getResult(rawValue, null, converter, defaultValueProvider, validators)
        }
    }
}
