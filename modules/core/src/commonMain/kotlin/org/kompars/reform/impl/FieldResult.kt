//package org.kompars.reform.impl
//
//import org.kompars.reform.*
//import org.kompars.reform.validation.*
//
//internal sealed interface FieldResult {
//    data class Value<T>(val value: T) : FieldResult
//    data class Errors(val index: Int? = null, val errors: List<ValidationError>) : FieldResult
//}
//
//internal suspend fun <T : Any> getResult(
//    rawValue: String?,
//    index: Int?,
//    converter: suspend (String?) -> T?,
//    defaultValueProvider: suspend () -> T?,
//    validators: List<suspend (T) -> ValidationError?>,
//): FieldResult {
//    val convertedValue = try {
//        rawValue?.let { converter(it) }
//    } catch (e: ConversionFailedException) {
//        return FieldResult.Errors(index, listOf(ValidationError(e.error)))
//    } catch (e: Exception) {
//        return FieldResult.Errors(index, listOf(ConversionFailed(e)))
//    }
//
//    val value = try {
//        convertedValue ?: defaultValueProvider()
//    } catch (e: MissingValueException) {
//        return FieldResult.Errors(index, listOf(MissingValue))
//    } catch (e: InvalidFormFieldException) {
//        return FieldResult.Errors(index, listOf(InvalidReferencedField(e.field)))
//    } catch (e: Exception) {
//        return FieldResult.Errors(index, listOf(DefaultValueProviderFailed(e)))
//    }
//
//    val errors = mutableListOf<ValidationError>()
//
//    if (value != null) {
//        for (validator in validators) {
//            val error = validate(value, validator)
//            if (error != null) {
//                errors += error
//            }
//        }
//    }
//
//    return if (errors.isEmpty()) {
//        FieldResult.Value(value)
//    } else {
//        FieldResult.Errors(index, errors)
//    }
//}
//
//internal suspend fun <T : Any> validate(value: T, validator: suspend (T) -> ValidationError?): ValidationError? {
//    return try {
//        validator(value)
//    } catch (e: InvalidFormFieldException) {
//        InvalidReferencedField(e.field)
//    } catch (e: Exception) {
//        ValidatorFailed(e)
//    }
//}
