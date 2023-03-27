package com.inn.test.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

//JSON Web Token and Spring Security belong to "User Management and Authentication" category of the tech stack.
//this @secuirty provide security to our application like authantication login page 

@SuppressWarnings("deprecation")
@Configuration // this will create bean whenever program runs
@EnableWebSecurity // @EnableWebSecurity to enable Spring Security's web security support and
					// provide the Spring MVC integration.

//WebSecurityConfigurerAdapter is an abstract class 
//but it doesn't contain any abstract method but contains default configurations.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomerUserDetailService customerUserDetailService;
     
	@Autowired
	JwtFilter jwtFilter;
	
	//we are here overriding the default AuthenticationManagerBuilder to add our own
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customerUserDetailService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
//It is used to configure how requests are secured by interceptors.
	// Here you can configure the HTTP request authentication detail.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(Request -> new CorsConfiguration().applyPermitDefaultValues())
		.and()
		.csrf().disable()//we donot need this as it is just for local user .
		.authorizeRequests()
		.antMatchers("/user/login", "/user/signup", "/user/forgotPassword")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
	//csrf -Cross-Site Request Forgery. It is an attack that forces an end user to execute unwanted 
	//actions on a web application in which they are currently authenticated.

	

}
