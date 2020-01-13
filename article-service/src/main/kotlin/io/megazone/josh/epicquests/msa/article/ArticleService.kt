package io.megazone.josh.epicquests.msa.article

import net.bytebuddy.utility.RandomString
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@EnableEurekaClient
@SpringBootApplication
class ArticleService

fun main(args: Array<String>) {
	runApplication<ArticleService>(*args)
}

@Entity
@Table(name = "ARTICLES")
data class Article(

	@Id
	val id: String = RandomString.make(30),

	var title: String? = null,

	@Lob
	var content: String? = null,

	val authorId: String? = null,

	val authorName: String? = null,

	@CreationTimestamp
	val createdAt: Date? = Date(),

	@UpdateTimestamp
	var updatedAt: Date? = null

)

@RepositoryRestResource(path = "articles")
interface ArticleRepository : JpaRepository<Article, String>
