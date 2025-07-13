package com.estore.k

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class KApplication

fun main(args: Array<String>) {
	runApplication<KApplication>(*args)
}
