package com.inn.test.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.inn.test.POJO.User;
import com.inn.test.wrapper.UserWrapper;

//JPA to store data in a relational database
public interface UserDao extends JpaRepository<User, Integer> {

	User FindByEmail(@Param("email") String email);//implemented by user class
	
	List<UserWrapper>getAllUser();
	
	List<String>getAllAdmin();
	
	@Transactional// these annotations used when we have to  modify or update
	@Modifying
	Integer updateStatus(@Param("status")String status, @Param("id") Integer id );
	
	//findByEmail is provided by JPA whenever we use With Pojo(User.java)
	User findByEmail(String email);
	
	
}
