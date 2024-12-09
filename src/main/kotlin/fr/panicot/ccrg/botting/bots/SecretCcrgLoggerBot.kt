package fr.panicot.ccrg.botting.bots

import fr.panicot.ccrg.botting.EasyBot
import fr.panicot.ccrg.botting.util.SchedulableTask
import fr.panicot.ccrg.core.messaging.Message
import fr.panicot.ccrg.core.messaging.MessageController
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.*


class SecretCcrgLoggerBot(messageController: MessageController, random: Random): EasyBot(messageController, random) {
    private val discordBot : JDA
    private val guild: Guild
    private val messageChannel: TextChannel

    init {
        val botToken = System.getenv("DISCORD_BOT_TOKEN")?:""
        this.discordBot = JDABuilder.createDefault(botToken, EnumSet.of(GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT))
            .addEventListeners(MessageListener(messageController))
            .build()
        this.guild = discordBot.awaitReady().getGuildById(139492617753722880)!!
        this.messageChannel = guild.getTextChannelById(139492617753722880)!!
    }

    override fun executeOnNewMessage(message: Message) {
        messageChannel.sendMessage("${message.author} said: ${message.content}")
            .queue()
    }

    override fun getTasksToSchedule(): Collection<SchedulableTask> {
        return Collections.emptyList()
    }

    class MessageListener(val messageController: MessageController): ListenerAdapter() {
        override fun onMessageReceived(event: MessageReceivedEvent) {
            if(event.author.isBot) return
            val message = event.message
            val content: String = message.contentRaw
            messageController.sendMessage(event.author.effectiveName, content)
        }
    }
}
