package com.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.jwt.dto.AuthRequest;
import com.jwt.model.User;
import com.jwt.service.JwtService;
import com.jwt.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
	
	
    private final UserService userService;
	
	@Autowired
	private  JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") //role base authorization
    public List<User> getAllUsers() {    	
    	
        return userService.getAllUsers();
    }

    @GetMapping("/data{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    
    //this handler is responsible to take username and password from client and generate token
    
    
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest request) {
    	
    	Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
    	
    	 //authenticationManger send UsernamePasswordToken object to ProviderManger and providerManager check with AuthenticationProvider and AuthenticationProvider check the usercrediation by comparing actual and database by calling UserDetailsService
    	 //and if authentication success or failure then it return Authentication object
    	
    	if(authenticate.isAuthenticated()) {
    		
    		return jwtService.generateToken(request.getUserName());  	
        	
    	}
    	else {
    		throw new UsernameNotFoundException("invalid username !!");
    	}
    	
    }
}
