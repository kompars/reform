package org.kompars.reform.file

import kotlin.jvm.JvmInline

@JvmInline
public value class BinarySize(public val bytes: Long) : Comparable<BinarySize> {
    public companion object {
        public val Int.kiloBytes: BinarySize
            get() = BinarySize(toLong() * 1024)

        public val Int.megaBytes: BinarySize
            get() = BinarySize(toLong() * 1024 * 1024)

        public val Int.gigaBytes: BinarySize
            get() = BinarySize(toLong() * 1024 * 1024 * 1024)
    }

    override fun compareTo(other: BinarySize): Int {
        return this.bytes.compareTo(other.bytes)
    }

    override fun toString(): String {
        return "$bytes B"
    }
}
