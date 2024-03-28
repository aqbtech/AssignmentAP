package com.hcmutap.elearning.config;


//import com.hcmutap.elearning.service.impl.CustomUserDetailService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//	@Resource
//	private CustomUserDetailService customUserDetailService;
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.httpBasic(Customizer.withDefaults())
				.csrf((csrf) -> csrf
						.ignoringRequestMatchers("/api/**")) // TODO: ignore CSRF for API
				.authorizeHttpRequests(
						authorizeRequests
								-> authorizeRequests
								.requestMatchers("/", "/trang-chu/**", "/api/**", "/login/**", "static/**", "templates/**","template/**")
								.permitAll()
								.requestMatchers("/admin-home")
								.hasAuthority("ADMIN")
								.requestMatchers("/edit")
								.hasAnyRole("ADMIN", "TEACHER")
								.anyRequest()
								.authenticated()
//								.permitAll() // TODO: authenticate but allow static resources to use thymeleaf
				)

				.formLogin(form -> form
						.loginProcessingUrl("/j_spring_security_check")
						.loginPage("/login")
						.defaultSuccessUrl("/login?success=true")
						.failureUrl("/login?error=true")
						.permitAll()
				);
		return httpSecurity.build();
	}

//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
//	}
}
