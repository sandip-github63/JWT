package com.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.filter.JwtAuthFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter authFilter;
	
	
	//Authentication
	@Bean
	public UserDetailsService userDetailService() {	
		
		return new ChildUserDetailsService(); 
	}
	
    //Authorization
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		System.out.println("inside securityfilterchain");
		
		return  http.csrf().disable()
		          .authorizeHttpRequests()
		          .requestMatchers("/api/users","/api/create","/api/authenticate")
		          .permitAll()
		          .and()
		          .authorizeHttpRequests().requestMatchers("/api/**").authenticated()
		          .and()
		          .sessionManagement()
		          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		          .and()
		          .authenticationProvider(authenticationProvider())
		          .addFilterBefore(authFilter,UsernamePasswordAuthenticationFilter.class).build();
		          
	}

	 
	@Bean
	public  PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		
		System.out.println("inside AuthenticationProvider");
		
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		
		authenticationProvider.setUserDetailsService(userDetailService());
		
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return  authenticationProvider;
				
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		
		return config.getAuthenticationManager();
	}
 
}
