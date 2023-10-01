package com.jwt.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.config.ChildUserDetailsService;
import com.jwt.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ChildUserDetailsService childUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token=null;
		String username=null;
		
		//get Token Bearer  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFqdSIsImlhdCI6MTY5NTkzNDk5MCwiZXhwIjoxNjk1OTM2NzkwfQ.ccSypfqD2LzSnxADFAUCovg707ajg_3v0tYeLespYy4
		
		String authHeader=request.getHeader("Authorization");
		
		System.out.println("header  :"+authHeader);
		
		if(authHeader !=null && authHeader.startsWith("Bearer ")) {
			//split Bearer and only get actual token  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFqdSIsImlhdCI6MTY5NTkzNDk5MCwiZXhwIjoxNjk1OTM2NzkwfQ.ccSypfqD2LzSnxADFAUCovg707ajg_3v0tYeLespYy4
			
			token=authHeader.substring(7);
			
			System.out.println("token  :"+token);
			
			username=jwtService.extractUsername(token);
			
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails = childUserDetailsService.loadUserByUsername(username);
			
			//here we have token username and username from database now we validate the token
			
			if(jwtService.validateToken(token, userDetails)) {
				//user validate successfully
				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
				
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
