package ru.panov.vk_ping_bot

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VkPingBotApplication

fun main(args: Array<String>) {
	val dotenv = dotenv {
		ignoreIfMissing = true
	}

	VkApiSettings.load(dotenv)

	runApplication<VkPingBotApplication>(*args)
}
