package ru.panov.vk_ping_bot

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class VkBotController {
    private val logger = LoggerFactory.getLogger(this::class.java)

    // посколько по заданию запрещено использовать готовые библиотеки реализующие VkApi
    // то от официального SDK отказался
    private val vkApiClient = MyVkApiClint(VkApiSettings.instance.accessToken)

    /*private val vk = VkApiClient(HttpTransportClient.getInstance())
    private val actor by lazy {
        GroupActor(
            VkApiSettings.instance.groupId,
            VkApiSettings.instance.accessToken,
        )
    }*/

    @PostMapping("/callback")
    fun handleCallback(
        @RequestBody event: CallbackEvent,
    ): String {
        logger.info(event.toString())

        if (event.secret != VkApiSettings.instance.secretKey) {
            logger.warn("Invalid secret key: ${event.secret}")
            return "invalid secret"
        }

        return when (event.type) {
            "confirmation" -> {
                logger.info("Confirmation request")
                VkApiSettings.instance.confirmationCode
            }
            "message_new" -> {
                val message = event.`object`!!.message
                logger.info("New message from ${message.fromId}: ${message.text}")

                sendMessage(message.fromId, "Вы сказали: ${message.text}")

                "ok"
            }
            else -> {
                logger.info("Unknown event type: ${event.type}")
                "unknown"
            }
        }
    }

    private fun sendMessage(userId: Int, text: String) {
        try {
            /*vk.messages()
                .send(actor)
                .userId(userId)
                .message(text)
                .randomId(Random.nextInt())
                .execute()*/

            vkApiClient.sendMessage(userId, text)

            logger.info("Message sent to $userId: $text")
        } catch (e: Exception) {
            logger.error("Failed to send message", e)
        }
    }
}
