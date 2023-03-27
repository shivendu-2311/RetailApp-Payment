package com.inn.test.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.test.JWT.CustomerUserDetailService;
import com.inn.test.JWT.JwtFilter;
import com.inn.test.JWT.JwtUtil;
import com.inn.test.POJO.User;
import com.inn.test.constents.CafeConstents;
import com.inn.test.dao.UserDao;
import com.inn.test.utils.CafeUtills;
import com.inn.test.utils.EmailUtills;
import com.inn.test.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j// it is used for login purpose
@Service// using restapi we will tell to project this is our service logic
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userDao;
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomerUserDetailService customerUserDetailService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	EmailUtills emailUtills;
	
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		System.out.print("inside SignUp {} :" + requestMap);
		
		try {
		if(validateSignUpMap(requestMap))
		{
			//to find the user is present or not
			User user = userDao.FindByEmail(requestMap.get("email"));
			//if user is not found in our DB then we create it
			if(Objects.isNull(user))
			{
				// here all the data is in requestMap but we can't pass it directly 
				userDao.save(this.UserGetFromRequestMap(requestMap));
		 return CafeUtills.getResponseEntity("User is Registered Sucessfully", HttpStatus.OK);
			}
			else//if user is already present
			{
				return CafeUtills.getResponseEntity("Email Already Exists", HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			return CafeUtills.getResponseEntity(CafeConstents.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
	}
	
	public boolean validateSignUpMap(Map<String, String> requestMap)
	{
		 if(requestMap.containsKey("name")&&requestMap.containsKey("contactNumber")&&
				requestMap.containsKey("email") && requestMap.containsKey("password"))
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	}
	//to extract user data from requestMap
	public User UserGetFromRequestMap(Map<String, String> requestMap)
	{
		User user = new User();
		
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");

		return user;
		
		
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
	
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password")));
			
			if(auth.isAuthenticated() == true)
			{
				//if user is approved by Admin
				if(customerUserDetailService.getUserDetails().getStatus().equalsIgnoreCase("true"))
				{
					return new ResponseEntity<String>("{\"token\":\""+ jwtUtil.genrateToken(customerUserDetailService.getUserDetails().getEmail(),
							customerUserDetailService.getUserDetails().getRole()) + "\"}", HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<String>("{\"message\":\""+"wait for Admin Approval. " +"\"}", HttpStatus.BAD_REQUEST);
				}
			}
			
		}
		catch(Exception ex){
			System.out.println("Inside login" + ex);
		}
		return new ResponseEntity<String>("{\"message\":\""+"Bad Fuckin Request. " +"\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>>getAllUser() {
		try {
			if(jwtFilter.isAdmin())
			{
				return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin())
			{
				java.util.Optional<User>optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				
				if(!optional.isEmpty())
				{
					userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
					return CafeUtills.getResponseEntity("Status updated SucessFully", HttpStatus.OK);
				}
				else
				{
					return CafeUtills.getResponseEntity("User id doesn't exist", HttpStatus.OK);
				}
			}
			else
			{
				return CafeUtills.getResponseEntity(CafeConstents.UNAUTHORIZED_ACCESS, HttpStatus.BAD_REQUEST);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity("Kuchh to garbad hai daya", HttpStatus.BAD_REQUEST);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
		
		allAdmin.remove(jwtFilter.getCurrentUser());
		
		if(status!= null && status.equalsIgnoreCase("true") )
		{
			emailUtills.sendSimpleMesssage(jwtFilter.getCurrentUser(), "Account Aprroved", "User:- "+user+"\n approved by \nADMIN:-"+jwtFilter.getCurrentUser() , allAdmin);
		}
		else
		{
			emailUtills.sendSimpleMesssage(jwtFilter.getCurrentUser(), "Account Disabled", "User:- "+user+"\n Disabled by \nADMIN:-"+jwtFilter.getCurrentUser() , allAdmin);
			
		}
		
	}

	@Override
	public ResponseEntity<String> checkToken() {
		
	return CafeUtills.getResponseEntity("true",HttpStatus.OK);
}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
	try {
		User userObj = userDao.FindByEmail(jwtFilter.getCurrentUser());
		if(!userObj.equals(null))
		{
			if(userObj.getPassword().equals(requestMap.get("oldPassword")))
			{
				userObj.setPassword(requestMap.get("newPassword"));
				userDao.save(userObj);
				return CafeUtills.getResponseEntity("Password Updated Sucessfully", HttpStatus.OK);
			}
			else
			{
				return CafeUtills.getResponseEntity("Incoreect_Password", HttpStatus.BAD_REQUEST);
			}
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return  CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	//4:29
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			User user = userDao.FindByEmail(requestMap.get("email"));
			if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
			emailUtills.forgotMail(user.getEmail(),"credential By CAFE Management",user.getPassword());
				
				return CafeUtills.getResponseEntity("Credentialss send to ur mail ", HttpStatus.OK);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtills.getResponseEntity(CafeConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}