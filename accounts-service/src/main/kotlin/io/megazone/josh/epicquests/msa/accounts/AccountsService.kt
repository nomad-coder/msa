package io.megazone.josh.epicquests.msa.accounts

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class AccountsService

fun main(args: Array<String>) {
	runApplication<AccountsService>(*args)
}

@RestController
@RequestMapping("/accounts")
class AccountsController {

	@Value("\${global.author}")
	private val globalAuthor: String? = null

	@Value("\${logging.level.org.springframework.web}")
	private val springLoggingLevel: String? = null

	@GetMapping
	fun list(): List<String> {
		println(globalAuthor)
		println(springLoggingLevel)
		return listOf("josh", "yoon")
	}

}