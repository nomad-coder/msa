package io.megazone.josh.epicquests.msa.accounts

import net.bytebuddy.utility.RandomString
import org.hibernate.annotations.CreationTimestamp
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import java.io.File
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@EnableEurekaClient
@SpringBootApplication
class AccountsService

fun main(args: Array<String>) {
	runApplication<AccountsService>(*args)
}

@Configuration
class RouteConfig(
	private val accountHandler: AccountHandler,
	private val testHandler: TestHandler
) {

	@Bean
	fun accountRoutes(): RouterFunction<ServerResponse> {
		return nest(path("/accounts"),
			router {
				GET("/", accountHandler::list)
			}
		)
	}

	@Bean
	fun testRoutes(): RouterFunction<ServerResponse> {
		return nest(path("/test"),
			router {
				GET("/author", testHandler::author)
				GET("/level", testHandler::loggingLevel)
			}
		)
	}

}

@Entity
@Table(name = "ACCOUNTS")
data class Account(

	@Id
	val id: String = RandomString.make(30),

	val username: String? = null,

	val password: String? = null,

	val email: String? = null,

	val name: String? = null,

	@CreationTimestamp
	val createdAt: Date? = Date()

)

interface AccountRepository: JpaRepository<Account, String>

@Component
class AccountHandler(
	private val repo: AccountRepository
) {

	fun list(request: ServerRequest): Mono<ServerResponse> = ok()
			.body(fromValue(repo.findAll()))
			.switchIfEmpty(notFound().build())

}

@RefreshScope
@Component
class TestHandler {

	@Value("\${global.author}")
	private val globalAuthor: String? = null

	@Value("\${logging.level.org.springframework.web}")
	private val loggingLevel: String? = null

	fun author(request: ServerRequest): Mono<ServerResponse> = ok().body(fromValue(globalAuthor!!))

	fun loggingLevel(request: ServerRequest): Mono<ServerResponse> = ok().body(fromValue(loggingLevel!!))

}
