package no.ntnu.ihb.fmi4j.export

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


internal fun getDateAndTime(): String {
    val now = LocalDateTime.now()
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now)
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").format(now)
    return "${dateFormat}T${timeFormat}Z"
}
