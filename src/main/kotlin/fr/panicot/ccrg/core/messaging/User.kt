package fr.panicot.ccrg.core.messaging

import java.time.Instant

/**
 * Created by William on 11/02/2017.
 */
data class User(val name: String, var afk: Boolean, var lastSeen: Instant)
