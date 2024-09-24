package org.kompars.reform

import kotlin.io.encoding.*
import kotlin.uuid.*

public fun Form.text(): RawField<String> {
    return createField { value ->
        when {
            value.contains("\n") -> throw ConversionFailedException("new_line_detected")
            else -> value
        }
    }
}

public fun Form.textArea(): RawField<String> {
    return createField { it }
}

public fun Form.boolean(): RawField<Boolean> {
    return createField { value ->
        value.toBooleanStrictOrNull() ?: throw ConversionFailedException("invalid_boolean")
    }
}

public fun Form.int(): RawField<Int> {
    return createField { value ->
        value.toIntOrNull() ?: throw ConversionFailedException("invalid_int")
    }
}

public fun Form.long(): RawField<Long> {
    return createField { value ->
        value.toLongOrNull() ?: throw ConversionFailedException("invalid_long")
    }
}

public inline fun <reified T : Enum<T>> Form.enum(crossinline block: (T) -> String? = { it.name }): RawField<T> {
    return createField { value ->
        enumValues<T>().firstOrNull { block(it) == value } ?: throw ConversionFailedException("invalid_enum_entry")
    }
}

public fun <T : Any> Form.enum(values: Map<String, T>): RawField<T> {
    return createField { value ->
        values[value] ?: throw ConversionFailedException("invalid_enum_entry")
    }
}

public fun <T : Any> Form.enum(vararg values: Pair<String, T>): RawField<T> {
    return enum(values.toMap())
}

@OptIn(ExperimentalEncodingApi::class)
public fun Form.base64(base64: Base64 = Base64): RawField<ByteArray> {
    return createField { value ->
        try {
            base64.decode(value)
        } catch (e: Exception) {
            throw ConversionFailedException("invalid_base64", e)
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
public fun Form.uuid(): RawField<Uuid> {
    return createField { value ->
        try {
            Uuid.parse(value)
        } catch (e: Exception) {
            throw ConversionFailedException("invalid_uuid", e)
        }
    }
}
