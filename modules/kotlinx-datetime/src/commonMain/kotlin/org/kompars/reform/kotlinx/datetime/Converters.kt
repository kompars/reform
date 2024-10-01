//package org.kompars.reform.kotlinx.datetime
//
//import kotlinx.datetime.*
//import org.kompars.reform.*
//
//public fun Form.instant(): RawField<Instant> {
//    return createField { Instant.Companion.parse(it) }
//}
//
//public fun Form.localDateTime(): RawField<LocalDateTime> {
//    return createField { LocalDateTime.Companion.parse(it) }
//}
//
//public fun Form.localDate(): RawField<LocalDate> {
//    return createField { LocalDate.Companion.parse(it) }
//}
//
//public fun Form.localTime(): RawField<LocalTime> {
//    return createField { LocalTime.Companion.parse(it) }
//}
//
//public fun RawField<LocalDateTime>.inTimeZone(timeZone: TimeZone): RawField<Instant> {
//    return convert { it.toInstant(timeZone) }
//}
