package fr.panicot.ccrg.core.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import fr.panicot.ccrg.core.CCRG_ZONE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class MessageTimestampTest {
    private val mapper: ObjectMapper = Jackson2ObjectMapperBuilder.json().build()

    @Test
    fun `timestamp is serialized as UTC epoch milliseconds`() {
        val instant = Instant.parse("2026-03-28T23:30:00Z")
        val json = mapper.writeValueAsString(Message(1L, instant, "alice", "hello"))

        // Epoch millis are independent of any server or client zone.
        assertTrue(json.contains("\"timestamp\":${instant.toEpochMilli()}"), json)
    }

    @Test
    fun `paris wall clock follows the spring and autumn transitions`() {
        // 2026-03-29 02:00 CET -> 03:00 CEST
        assertEquals("00:30", parisTime("2026-03-28T23:30:00Z"))
        assertEquals("01:30", parisTime("2026-03-29T23:30:00Z"))

        // 2026-10-25 03:00 CEST -> 02:00 CET
        assertEquals("01:30", parisTime("2026-10-24T23:30:00Z"))
        assertEquals("00:30", parisTime("2026-10-25T23:30:00Z"))
    }

    @Test
    fun `the same instant renders differently in another user zone`() {
        val instant = Instant.parse("2026-10-25T23:30:00Z")

        assertEquals("00:30", format(instant, CCRG_ZONE))
        assertEquals("19:30", format(instant, ZoneId.of("America/New_York")))
    }

    private fun parisTime(instant: String): String = format(Instant.parse(instant), CCRG_ZONE)

    private fun format(instant: Instant, zone: ZoneId): String {
        val time = ZonedDateTime.ofInstant(instant, zone).toLocalTime()
        return "%02d:%02d".format(time.hour, time.minute)
    }
}
