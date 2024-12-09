package fr.panicot.ccrg.security

import fr.panicot.ccrg.security.authentication.CCRGAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain


/**
 * Created by William on 11/02/2017.
 */

@Configuration
@EnableWebSecurity
open class WebSecurityConfig {

    @Throws(Exception::class)
    @Bean
    open fun filterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        return http
            .csrf {
                it.disable()
            }
            .authorizeHttpRequests() {
                it.requestMatchers("/js/**").permitAll()
                    .requestMatchers("/css/**").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    .anyRequest().hasRole("USER")
            }
            .formLogin() {
                it.loginPage("/login")
                    .failureUrl("/login")
                    .permitAll()
            }
            .build()
    }

    @Bean
    open fun ccrgAuthenticationProvider(): AuthenticationProvider {
        val defaultPassword = System.getenv("DEFAULT_PASSWORD") ?: "Raviolis 4Ever"
        return CCRGAuthenticationProvider(defaultPassword)
    }
}