package com.estore.j.repository

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
        private val jdbcClient: JdbcClient
)
