package org.kompars.reform.file.validation

import org.kompars.reform.*
import org.kompars.reform.file.*
import org.kompars.reform.validation.*

public data object ImageWidthUnknown : ValidationError("image_width_unknown")

public data class ImageWidthTooSmall(val minWidth: Int) :
    ValidationError("image_width_too_small", "minWidth" to minWidth)

public fun <T : ImageMetadata?> SingleField<T>.minWidth(minWidth: Int): SingleField<T> {
    return addValidator { file ->
        when {
            file.width == null -> ImageWidthUnknown
            file.width!! < minWidth -> ImageWidthTooSmall(minWidth)
            else -> null
        }
    }
}

public data class ImageWidthTooBig(val maxWidth: Int) :
    ValidationError("image_width_too_big", "maxWidth" to maxWidth)

public fun <T : ImageMetadata?> SingleField<T>.maxWidth(maxWidth: Int): SingleField<T> {
    return addValidator { file ->
        when {
            file.width == null -> ImageWidthUnknown
            file.width!! > maxWidth -> ImageWidthTooBig(maxWidth)
            else -> null
        }
    }
}

public data object ImageHeightUnknown : ValidationError("image_height_unknown")

public data class ImageHeightTooSmall(val minHeight: Int) :
    ValidationError("image_height_too_small", "minHeight" to minHeight)

public fun <T : ImageMetadata?> SingleField<T>.minHeight(minHeight: Int): SingleField<T> {
    return addValidator { file ->
        when {
            file.height == null -> ImageHeightUnknown
            file.height!! < minHeight -> ImageHeightTooSmall(minHeight)
            else -> null
        }
    }
}

public data class ImageHeightTooBig(val maxHeight: Int) :
    ValidationError("image_height_too_big", "maxHeight" to maxHeight)

public fun <T : ImageMetadata?> SingleField<T>.maxHeight(maxHeight: Int): SingleField<T> {
    return addValidator { file ->
        when {
            file.height == null -> ImageHeightUnknown
            file.height!! > maxHeight -> ImageHeightTooBig(maxHeight)
            else -> null
        }
    }
}
