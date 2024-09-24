package org.kompars.reform.file.validation

import org.kompars.reform.*
import org.kompars.reform.file.*
import org.kompars.reform.validation.*

public data class FileSizeTooSmall(val minSize: BinarySize) :
    ValidationError("file_size_too_small", "minSize" to minSize)

public fun <T : FileMetadata?> SingleField<T>.minSize(minSize: BinarySize): SingleField<T> {
    return addValidator { file ->
        when {
            file.size < minSize.bytes -> FileSizeTooSmall(minSize)
            else -> null
        }
    }
}

public data class FileSizeTooBig(val maxSize: BinarySize) :
    ValidationError("file_size_too_big", "maxSize" to maxSize)

public fun <T : FileMetadata?> SingleField<T>.maxSize(maxSize: BinarySize): SingleField<T> {
    return addValidator { file ->
        when {
            file.size > maxSize.bytes -> FileSizeTooBig(maxSize)
            else -> null
        }
    }
}

public data class InvalidFileExtension(val extensions: Iterable<String>) :
    ValidationError("invalid_file_extension", "extensions" to extensions)

public fun <T : FileMetadata?> SingleField<T>.extension(vararg extensions: String): SingleField<T> {
    return addValidator { file ->
        when {
            extensions.none { file.extension.equals(it, true) } -> InvalidFileExtension(extensions.toList())
            else -> null
        }
    }
}
