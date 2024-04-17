package com.hcmutap.elearning.config;

import com.hcmutap.elearning.exception.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return new CustomAccessDeniedHandler();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.httpBasic(Customizer.withDefaults())
				.csrf((csrf) -> csrf
						.ignoringRequestMatchers("/api/**")) // TODO: ignore CSRF for API
				.authorizeHttpRequests(
						authorizeRequests
								-> authorizeRequests
								.requestMatchers("/", "/trang-chu/**", "/api/**", "/login", "/logout", "static/**", "templates/**","template/**")
								.permitAll()
								.requestMatchers("/admin-**/**")
//								.requestMatchers("/admin-**")
								.hasAuthority("ADMIN")
//								.requestMatchers("/edit")
//								.hasAnyRole("ADMIN", "TEACHER")
								.anyRequest()
								.authenticated()
//								.permitAll() // TODO: authenticate but allow static resources to use thymeleaf
				)
				.exceptionHandling(page -> page
						.accessDeniedHandler(accessDeniedHandler())
						.accessDeniedPage("/accessDenied")
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/trang-chu")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID"))
				.formLogin(form -> form
						.loginProcessingUrl("/j_spring_security_check")
						.loginPage("/login")
						.defaultSuccessUrl("/trang-chu")
						.failureUrl("/login?error=true")
						.permitAll()
				);
		return httpSecurity.build();
	}
}
