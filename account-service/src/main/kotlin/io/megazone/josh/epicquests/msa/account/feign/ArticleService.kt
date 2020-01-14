package io.megazone.josh.epicquests.msa.account.feign

import feign.hystrix.FallbackFactory
import io.megazone.josh.epicquests.msa.account.Account
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "article-service", path = "/articles", fallbackFactory = ArticleServiceFallbackFactory::class)
interface ArticleService {

	@PatchMapping("/sync/account-name")
	fun syncAccountName(@RequestBody account: Account)

}

@Component
internal class ArticleServiceFallbackFactory : FallbackFactory<ArticleService> {

	private val logger = LoggerFactory.getLogger(this.javaClass)

	override fun create(cause: Throwable): ArticleService {
		return object : ArticleService {
			override fun syncAccountName(account: Account) {
				logger.error("Fallback $account", cause)
			}
		}
	}

}
