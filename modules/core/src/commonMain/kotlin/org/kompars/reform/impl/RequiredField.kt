package org.kompars.reform.impl

import org.kompars.reform.*
import org.kompars.reform.validation.*

internal class RequiredFieldImpl<T : Any>(
    private val form: FormImpl,
    private val converter: suspend (String?) -> T?,
    private val defaultValueProvider: suspend () -> T,
    private val validators: List<suspend (T) -> ValidationError?> = mutableListOf(),
) : RequiredField<T> {
    override fun collection(): CollectionField<T> {
        return CollectionFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = defaultValueProvider,
            itemValidators = validators,
        )
    }

    override fun addValidator(block: suspend (T) -> ValidationError?): RequiredField<T> {
        return RequiredFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = defaultValueProvider,
            validators = validators + block,
        )
    }

    override fun bind(name: String): Lazy<T> {
        return form.register(name) { rawValues ->
            val rawValue = when (rawValues.size) {
                0, 1 -> rawValues.singleOrNull()
                else -> return@register FieldResult.Errors(null, listOf(SingleValueExpected))
            }

            getResult(rawValue, null, converter, defaultValueProvider, validators)
        }
    }
}
