package io.megazone.josh.epicquests.msa.account.feign

import io.megazone.josh.epicquests.msa.account.Account
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "article-service", path = "/articles")
interface ArticleService {

	@PatchMapping("/sync/account-name")
	fun syncAccountName(@RequestBody account: Account)

}