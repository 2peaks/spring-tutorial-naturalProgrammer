package com.naturalprogrammer.spring.tutorial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naturalprogrammer.spring.tutorial.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	//Spring repositiry provide the implementation using the field names :)
	
	//@Query("select u from User u where u.email = ?1") <!-- if prefer to write your own sql
	//@Query(value = "select * from users where email = ?0", nativeQuery = true)
	
	//@Query("select u from User u where u.email = :email")
	//User findByEmail(@Param("email") String email)
	
	User findByEmail(String email);

	User findByForgotPasswordCode(String forgotPasswordCode);
}
