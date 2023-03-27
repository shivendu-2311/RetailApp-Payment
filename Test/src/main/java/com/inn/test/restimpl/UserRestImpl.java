package com.inn.test.restimpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.test.constents.CafeConstents;
import com.inn.test.rest.UserRest;
import com.inn.test.services.UserService;
import com.inn.test.utils.CafeUtills;
import com.inn.test.wrapper.UserWrapper;

//this is used to build rest api's in declarative wa
@RestController
public class UserRestImpl implements UserRest {

	//injecting beans'(Objects) at runtime by Spring Dependency Injection mechanism 
	@Autowired
	UserService userService;
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
	
	
		try {
			//passing data recived in requestMap to service
			return userService.signUp(requestMap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		
		try {
			return userService.login(requestMap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			return userService.getAllUser();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
	
		try {
			return userService.update(requestMap);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> checkToken() {
		try {
			return userService.checkToken();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	 return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			return userService.changePassword(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	 return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			return userService.forgotPassword(requestMap);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	 return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
