package io.megazone.josh.epicquests.msa.account

import net.bytebuddy.utility.RandomString
import org.hibernate.annotations.CreationTimestamp
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@EnableEurekaClient
@SpringBootApplication
class AccountService

fun main(args: Array<String>) {
	runApplication<AccountService>(*args)
}

@RestController
@RequestMapping("/accounts")
class AccountController(
	private val repo: AccountRepository
) {

	@PostMapping
	fun create(@RequestBody account: Account): Account {
		return repo.save(account)
	}

	@GetMapping
	fun list(): List<Account> {
		return repo.findAll()
	}

	@GetMapping("/{id}")
	fun details(@PathVariable id: String): Account {
		return repo.findById(id).get()
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: String) {
		return repo.deleteById(id)
	}

}

@RefreshScope
@RestController
@RequestMapping("/test")
class TestController(
	@Value("\${global.author}") private val globalAuthor: String
) {

	@GetMapping("/author")
	fun author(): String {
		return globalAuthor
	}

}

@Entity
@Table(name = "ACCOUNTS")
data class Account(

	@Id
	val id: String = RandomString.make(30),

	val username: String? = null,

	var password: String? = null,

	var email: String? = null,

	var name: String? = null,

	@CreationTimestamp
	val createdAt: Date? = Date()

)

interface AccountRepository : JpaRepository<Account, String>
