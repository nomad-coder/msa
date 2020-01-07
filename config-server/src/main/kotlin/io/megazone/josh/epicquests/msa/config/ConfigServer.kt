package io.megazone.josh.epicquests.msa.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@SpringBootApplication
class ConfigServer

fun main(args: Array<String>) {
	runApplication<ConfigServer>(*args)
}