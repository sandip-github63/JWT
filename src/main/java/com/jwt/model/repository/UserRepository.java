package com.jwt.model.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.jwt.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserName(String username);
	
	
	

}
