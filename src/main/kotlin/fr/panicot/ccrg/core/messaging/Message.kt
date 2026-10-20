package fr.panicot.ccrg.core.messaging

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Instant

/**
 * Created by William on 11/02/2017.
 *
 * [timestamp] is an absolute instant. It is serialized as UTC epoch milliseconds so
 * clients can convert it to their own zone without any server-side offset arithmetic.
 */
data class Message(
    val id: Long,
    @get:JsonFormat(
        shape = JsonFormat.Shape.NUMBER,
        without = [JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS]
    )
    val timestamp: Instant,
    val author: String,
    val content: String,
    val recipient: String? = null,
    val specialType: String? = null
)
