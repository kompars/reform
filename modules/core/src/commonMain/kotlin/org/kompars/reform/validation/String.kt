package org.kompars.reform.validation

import org.kompars.reform.*

public data class StringTooShort(val minLength: Int) :
    ValidationError("string_too_short", "minLength" to minLength)

public fun <T : String?> SingleField<T>.minLength(minLength: Int): SingleField<T> {
    return addValidator { value ->
        when {
            value.length < minLength -> StringTooShort(minLength)
            else -> null
        }
    }
}

public data class StringTooLong(val maxLength: Int) :
    ValidationError("string_too_long", "maxLength" to maxLength)

public fun <T : String?> SingleField<T>.maxLength(maxLength: Int): SingleField<T> {
    return addValidator { value ->
        when {
            value.length > maxLength -> StringTooLong(maxLength)
            else -> null
        }
    }
}

public data object InvalidPattern : ValidationError("invalid_pattern")

public fun <T : String?> SingleField<T>.matches(regex: Regex): SingleField<T> {
    return addValidator { value ->
        when {
            value.matches(regex) -> null
            else -> InvalidPattern
        }
    }
}

public data object InvalidEmail : ValidationError("invalid_email") {
    public val REGEX: Regex =
        "^[a-z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?(?:\\.[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?)+\$".toRegex()
}

public fun <T : String?> SingleField<T>.email(): SingleField<T> {
    return addValidator { value ->
        when {
            value.matches(InvalidEmail.REGEX) -> null
            else -> InvalidEmail
        }
    }
}

public data object InvalidUrl : ValidationError("invalid_url") {
    public val REGEX: Regex =
        "^https?://[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*(?:\\\\:[0-9]{2,4})?(?:/.*)?\$".toRegex()
}

public fun <T : String?> SingleField<T>.url(): SingleField<T> {
    return addValidator { value ->
        when {
            value.matches(InvalidUrl.REGEX) -> null
            else -> InvalidUrl
        }
    }
}

public data object NotRelativeUrl : ValidationError("not_relative_url")

public fun <T : String?> SingleField<T>.relativeUrl(): SingleField<T> {
    return addValidator { value ->
        when {
            !value.startsWith("/") || value.startsWith("//") -> NotRelativeUrl
            else -> null
        }
    }
}

public data object UppercaseLetterMissing : ValidationError("uppercase_letter_missing")

public fun <T : String?> SingleField<T>.containsUpperCaseLetter(): SingleField<T> {
    return addValidator { value ->
        when {
            value.none { it.isUpperCase() } -> UppercaseLetterMissing
            else -> null
        }
    }
}

public data object LowercaseLetterMissing : ValidationError("lowercase_letter_missing")

public fun <T : String?> SingleField<T>.containsLowerCaseLetter(): SingleField<T> {
    return addValidator { value ->
        when {
            value.none { it.isLowerCase() } -> ValidationError("lowercase_letter_missing")
            else -> null
        }
    }
}

public data object DigitMissing : ValidationError("digit_missing")

public fun <T : String?> SingleField<T>.containsDigit(): SingleField<T> {
    return addValidator { value ->
        when {
            value.none { it.isDigit() } -> DigitMissing
            else -> null
        }
    }
}

public data object SpecialCharacterMissing : ValidationError("special_character_missing")

public fun <T : String?> SingleField<T>.containsSpecialCharacter(): SingleField<T> {
    return addValidator { value ->
        when {
            value.none { !it.isLetterOrDigit() } -> SpecialCharacterMissing
            else -> null
        }
    }
}

public fun <T : String?> SingleField<T>.strongPassword(minLength: Int = 8): SingleField<T> {
    return minLength(minLength)
        .containsDigit()
        .containsUpperCaseLetter()
        .containsLowerCaseLetter()
        .containsSpecialCharacter()
}
