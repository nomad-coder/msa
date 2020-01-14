package io.megazone.josh.epicquests.msa.article.message

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.megazone.josh.epicquests.msa.article.Account
import io.megazone.josh.epicquests.msa.article.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class MessageReceiver(
	private val accountRepo: AccountRepository
) {

	private val logger = LoggerFactory.getLogger(this.javaClass)

	@RabbitListener(queues = ["SYNC-ACCOUNT-NAME"])
	fun processSyncAccountName(content: String) {
		logger.info("received from rabbitmq $content")
		val account = jacksonObjectMapper().readValue<Account>(content)
		accountRepo.save(account)
	}

}