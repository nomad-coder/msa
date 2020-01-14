package io.megazone.josh.epicquests.msa.account

import io.megazone.josh.epicquests.msa.account.feign.ArticleService
import net.bytebuddy.utility.RandomString
import org.hibernate.annotations.CreationTimestamp
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty
import javax.validation.groups.Default

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
class AccountServiceApplication

fun main(args: Array<String>) {
	runApplication<AccountServiceApplication>(*args)
}

@RestController
@RequestMapping("/")
class AccountController(
	private val service: AccountService,
	private val repo: AccountRepository
) {

	@PostMapping
	fun create(@RequestBody account: Account) = repo.save(account)

	@GetMapping
	fun list(): List<Account> = repo.findAll()

	@GetMapping("/{id}")
	fun details(@PathVariable id: String) = repo.findById(id).get()

	@PatchMapping("/{id}/name")
	fun patchName(@PathVariable id: String,
				  @Validated(value = [ValidationGroups.UpdateName::class]) @RequestBody account: Account, bindingResult: BindingResult) {
		if (bindingResult.hasErrors()) {
			throw BindException(bindingResult)
		}
		service.updateName(id, account.name!!)
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: String) = repo.deleteById(id)

}

@RefreshScope
@RestController
@RequestMapping("/test")
class TestController(
	@Value("\${global.author}") private val globalAuthor: String
) {

	@GetMapping("/author")
	fun author() = globalAuthor

}

@Entity
@Table(name = "ACCOUNTS")
data class Account(

	@Id
	val id: String = RandomString.make(30),

	val username: String? = null,

	var password: String? = null,

	var email: String? = null,

	@get:NotEmpty(groups = [ValidationGroups.UpdateName::class])
	var name: String? = null,

	@CreationTimestamp
	val createdAt: Date? = Date()

)

@Service
class AccountService(
	private val repo: AccountRepository,
	private val articleService: ArticleService
) {

	fun updateName(id: String, name: String) {
		val account = repo.findById(id).get().apply {
			this.name = name
		}
		repo.save(account)

		articleService.syncAccountName(account)    //TODO : 이런걸 메시지로 해야 하는 건가!?
	}

}

interface AccountRepository : JpaRepository<Account, String>

object ValidationGroups {

	interface UpdateName : Default

}