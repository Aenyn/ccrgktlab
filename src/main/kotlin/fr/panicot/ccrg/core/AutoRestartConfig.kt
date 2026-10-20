package fr.panicot.ccrg.core

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import java.time.ZoneId

/**
 * Zone used to interpret every cron expression on the server.
 *
 * Instants themselves are always stored in UTC; this zone only applies to calendar
 * rules such as bot schedules and "today".
 */
val CCRG_ZONE: ZoneId = ZoneId.of("Europe/Paris")

/**
 * Created by William on 26/03/2017.
 */
@Configuration
open class AutoRestartConfig {

    val executor = ThreadPoolTaskScheduler()

    @Bean
    open fun scheduleRestart(): Boolean {
        executor.initialize()
        executor.schedule(Runnable{System.exit(0)}, CronTrigger("0 0 3 * * *", CCRG_ZONE))
        return true
    }
}