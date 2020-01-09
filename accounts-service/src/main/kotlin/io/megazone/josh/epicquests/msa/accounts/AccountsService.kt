package io.megazone.josh.epicquests.msa.accounts

import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
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

@EnableEurekaClient
@SpringBootApplication
class AccountsService

fun main(args: Array<String>) {
	runApplication<AccountsService>(*args)
}

@Configuration
@Profile("local")
class LocalConfig {

	@Bean(initMethod = "start", destroyMethod = "stop")
	fun h2Server(): Server {
		val server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")

		return server
	}

}

@Configuration
class RouteConfig(
	private val handler: AccountsHandler
) {

	@Bean
	fun routes(): RouterFunction<ServerResponse> {
		return nest(path("/accounts"),
			router {
				GET("/", handler::list)
				GET("/author", handler::author)
				GET("/level", handler::loggingLevel)
			}
		)
	}

}

@RefreshScope
@Component
class AccountsHandler {

	@Value("\${global.author}")
	private val globalAuthor: String? = null

	@Value("\${logging.level.org.springframework.web}")
	private val loggingLevel: String? = null

	fun author(request: ServerRequest): Mono<ServerResponse> = ok().body(fromValue(globalAuthor!!))

	fun loggingLevel(request: ServerRequest): Mono<ServerResponse> = ok().body(fromValue(loggingLevel!!))

	fun list(request: ServerRequest): Mono<ServerResponse> = ok()
			.body(fromValue(listOf("Josh", "Yoon")))
			.switchIfEmpty(notFound().build())

}
