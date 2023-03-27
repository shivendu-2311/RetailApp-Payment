package com.inn.test.JWT;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;



//filtering is used to check the user is authenticated and checked on various context
@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomerUserDetailService service;
	
	
	Claims claims = null;
	private String userName = null;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		//if the user url is matches
		if(httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup"))
		{
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		else
		{
			String authorizationnHeader = httpServletRequest.getHeader("Authorization");
			String token = null;
			
			if(authorizationnHeader != null && authorizationnHeader.startsWith("Bearer "))
			{
				token = authorizationnHeader.substring(7);
				userName = jwtUtil.extractUsername(token);
				claims = jwtUtil.extractAllClaims(token);
				
			}
			
			//tpinau
			if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null)
			{
				UserDetails userDetails = service.loadUserByUsername(userName);
				if(jwtUtil.validateToken(token, userDetails))
				{
					UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken = 
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					userNamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					
					//setting the url
					SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);
				}
			}
			// after checking all this we let user pass
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		
	}
	
	//to check user has rights or not
	public Boolean isAdmin()
	{
		return "admin".equalsIgnoreCase((String)(claims.get("role")));
	}
	public Boolean isUser()
	{
		return "user".equalsIgnoreCase((String)(claims.get("role")));
	}
	
	public String getCurrentUser()
	{
		return userName;
	}

}
