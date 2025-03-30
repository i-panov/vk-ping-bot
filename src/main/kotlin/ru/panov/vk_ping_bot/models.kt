package ru.panov.vk_ping_bot

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.cdimascio.dotenv.Dotenv

data class VkApiSettings (
    val accessToken: String,
    val groupId: Int,
    val confirmationCode: String,
    val secretKey: String,
) {
    companion object {
        val instance: VkApiSettings
            get() {
                if (_instance == null) {
                    throw RuntimeException("VkApiSettings is not loaded!")
                }

                return _instance!!
            }

        private var _instance: VkApiSettings? = null

        fun load(dotenv: Dotenv) {
            _instance = VkApiSettings(
                accessToken = dotenv["VK_ACCESS_TOKEN"]!!,
                groupId = dotenv["VK_GROUP_ID"]!!.toInt(),
                confirmationCode = dotenv["VK_CONFIRMATION_CODE"]!!,
                secretKey = dotenv["VK_SECRET_KEY"]!!,
            )
        }
    }
}

data class CallbackEvent(
    val type: String,
    val secret: String?,
    val `object`: MessageObject?
)

data class MessageObject(
    val message: VkMessage
)

data class VkMessage(
    val id: Int,

    @JsonProperty("from_id")
    val fromId: Int,

    val text: String,
)
