package com.inn.test.JWT;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inn.test.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUserDetailService implements UserDetailsService {

	@Autowired
	UserDao userDao;

	private com.inn.test.POJO.User userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Inside Log UserDetails" + username);
		userDetail = userDao.FindByEmail(username);

		if (!Objects.isNull(userDetail)) {
			return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User Not Found");
		}

	}

	public com.inn.test.POJO.User getUserDetails() {
		return userDetail;
	}

}
