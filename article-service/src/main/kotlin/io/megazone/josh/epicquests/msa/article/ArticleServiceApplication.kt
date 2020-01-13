package io.megazone.josh.epicquests.msa.article

import net.bytebuddy.utility.RandomString
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.persistence.*

@EnableEurekaClient
@SpringBootApplication
class ArticleServiceApplication

fun main(args: Array<String>) {
	runApplication<ArticleServiceApplication>(*args)
}

@RestController
@RequestMapping("/articles/sync")
class SyncController(
	private val accountRepo: AccountRepository
) {

	@PatchMapping("/account-name")
	fun syncAccountName(@RequestBody account: Account) {
		accountRepo.save(account)
	}

}


@Entity
@Table(name = "ARTICLES")
data class Article(

	@Id
	val id: String = RandomString.make(30),

	var title: String? = null,

	@Lob
	var content: String? = null,

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
	@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")
	var author: Account? = null,

	@CreationTimestamp
	val createdAt: Date? = Date(),

	@UpdateTimestamp
	var updatedAt: Date? = null

)

@Entity
@Table(name = "ACCOUNTS")
data class Account(

	@Id
	val id: String? = null,

	var name: String? = null

)

@RepositoryRestResource(path = "articles")
interface ArticleRepository : JpaRepository<Article, String>

interface AccountRepository : JpaRepository<Account, String>