package com.plzhans.assignment.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type A application config.
 */
@Configuration
@EnableJpaRepositories({"com.plzhans.assignment.common.repository","com.plzhans.assignment.api.repository"})
@EnableJpaAuditing
@EntityScan({"com.plzhans.assignment.common.entity","com.plzhans.assignment.api.entity"})
public class ApplicationConfig {

}
