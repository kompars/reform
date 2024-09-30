package org.kompars.reform.validation

public open class ValidationError(
    public val message: String,
    public val params: Map<String, Any?> = emptyMap(),
) {
    public constructor(message: String, vararg params: Pair<String, Any?>) : this(message, mapOf(*params))

    override fun toString(): String {
        return "ValidationError(message=$message, params=$params)"
    }
}

public data object MissingValue : ValidationError("missing_value")

public data object SingleValueExpected : ValidationError("single_value_expected")

public data class ConversionFailed(val exception: Exception) :
    ValidationError(
        "conversion_failed",
        "exception" to exception::class.simpleName,
        "message" to exception.message
    )

public data class DefaultValueProviderFailed(val exception: Exception) :
    ValidationError(
        "default_value_provider_failed",
        "exception" to exception::class.simpleName,
        "message" to exception.message
    )

public data class ValidatorFailed(val exception: Exception) :
    ValidationError(
        "validator_failed",
        "exception" to exception::class.simpleName,
        "message" to exception.message
    )

public data class InvalidReferencedField(val field: String) :
    ValidationError("invalid_referenced_field", "field" to field)

public data class InvalidCollectionItems(val errors: Map<Int, List<ValidationError>>) :
    ValidationError("invalid_collection_items", "errors" to errors.size)
