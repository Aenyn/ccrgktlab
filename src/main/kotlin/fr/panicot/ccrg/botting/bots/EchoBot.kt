package fr.panicot.ccrg.botting.bots

import fr.panicot.ccrg.botting.EasyBot
import fr.panicot.ccrg.botting.util.SchedulableTask
import fr.panicot.ccrg.core.messaging.Message
import fr.panicot.ccrg.core.messaging.MessageController
import fr.panicot.ccrg.core.messaging.SYSTEM_ANNOUNCEMENT
import java.time.Duration
import java.time.Instant
import java.util.*

class EchoBot(messageController: MessageController, random: Random): EasyBot(messageController, random) {
    var currentName = "EchoBot"
    var lastMessage: Instant? = null

    override fun executeOnNewMessage(message: Message) {
        if (message.author == SYSTEM_ANNOUNCEMENT || message.author.endsWith("Bot")) return

        val now = Instant.now()
        val previousMessage = lastMessage

        // don't spam links
        if (previousMessage !== null &&
                Duration.between(previousMessage, now) > Duration.ofHours(2) &&
                !message.content.contains("<a target")) {
            Thread.sleep(500L )
            messageController.announceArrival(currentName, true)
            Thread.sleep(2000L )
            messageController.sendMessage(currentName, subs(message.content, 20) + "...")
            Thread.sleep(2500L )
            messageController.sendMessage(currentName, subs(message.content.toLowerCase(), 13) + "...")
            Thread.sleep(2500L )
            messageController.sendMessage(currentName, subs(message.content.toLowerCase(), 7) + "...")
            Thread.sleep(2500L )
            messageController.announceArrival(currentName, false)
        }
        lastMessage = now
    }

    override fun getTasksToSchedule(): Collection<SchedulableTask> {
        return Collections.emptyList()
    }

    private fun subs(text: String, length: Int): String {
        return if (text.length <= length) text else text.substring(text.length - length)
    }
}
