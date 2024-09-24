package org.kompars.reform.impl

import org.kompars.reform.*
import org.kompars.reform.validation.*

internal class CollectionFieldImpl<T>(
    private val form: FormImpl,
    private val converter: (String?) -> T?,
    private val defaultValueProvider: () -> T,
    private val itemValidators: List<(T & Any) -> ValidationError?> = mutableListOf(),
    private val listValidators: List<(List<T>) -> ValidationError?> = mutableListOf(),
) : CollectionField<T> {
    override fun addValidator(block: (List<T>) -> ValidationError?): CollectionField<T> {
        return CollectionFieldImpl(
            form = form,
            converter = converter,
            defaultValueProvider = defaultValueProvider,
            itemValidators = itemValidators,
            listValidators = listValidators + block,
        )
    }

    override fun bind(name: String): Lazy<List<T>> {
        return form.register(name) { rawValues ->
            val results = rawValues.mapIndexed { index, rawValue ->
                getResult(rawValue, index, converter, defaultValueProvider, itemValidators)
            }

            val values = results
                .filterIsInstance<FieldResult.Value<T>>()
                .map { it.value }

            val itemErrors = results
                .filterIsInstance<FieldResult.Errors>()
                .associateBy({ it.index!! }) { it.errors }

            val errors = mutableListOf<ValidationError>()

            if (itemErrors.isNotEmpty()) {
                errors += InvalidCollectionItems(itemErrors)
            } else {
                for (validator in listValidators) {
                    val error = validate(values, validator)
                    if (error != null) {
                        errors += error
                    }
                }
            }

            if (errors.isEmpty()) {
                FieldResult.Value(values)
            } else {
                FieldResult.Errors(null, errors)
            }
        }
    }
}
