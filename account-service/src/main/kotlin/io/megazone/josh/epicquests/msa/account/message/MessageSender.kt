package io.megazone.josh.epicquests.msa.account.message

import com.fasterxml.jackson.databind.ObjectMapper
import io.megazone.josh.epicquests.msa.account.Account
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class MessageSender(
	private val messagingTemplate: RabbitMessagingTemplate,
	private val objectMapper: ObjectMapper
) {

	companion object {
		private const val SYNC_NAME_QUEUE = "SYNC-ACCOUNT-NAME"
	}

	@Bean
	fun syncNameQueue(): Queue {
		return Queue(SYNC_NAME_QUEUE)
	}

	fun syncName(account: Account) {
		messagingTemplate.convertAndSend(SYNC_NAME_QUEUE, objectMapper.writeValueAsString(account))
	}

}
