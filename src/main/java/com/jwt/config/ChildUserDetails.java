package com.jwt.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jwt.model.User;

public class ChildUserDetails implements UserDetails{
	
	
	//copy all user data 
	
	    private String userName;
	     
	    private String password;
	    
	    private String email;
	    
	    private List<GrantedAuthority> authorities;//list of role copy
	    
	    public ChildUserDetails(User user) {
	    	
	    	System.out.println("inside childuserdetails");
	    	
	    	this.userName=user.getUserName();
	    	this.password=user.getPassword();
	    	this.email=user.getEmail();
	    	this.authorities=Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new)
	    			.collect(Collectors.toList());
	    	
	    	
	    	
	    }
	   
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
