    //JWT is used when a user signup for first time JWT gives a token to him so that when he next time visit he can submit the token and 
//enter`
//Claim- when we are reffering claim it means fields present in that
package com.inn.test.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//@service - real functionalties is written here
@Service
public class JwtUtil {

	private String secret = "btechdays";
	
	//to extract username
	public String extractUsername(String token)
	{
		return extractClaims(token, Claims::getSubject);
	}
	public Date extractExpiration(String token)
	{
		return extractClaims(token, Claims::getExpiration);
		//:: use to refer to call to getEExpiration with reference of Claims class
	}

	//<T> is generic of type T;
	//claims are pieces of information asserted about a subject ex token name = "JOhn DOe"
	public <T> T extractClaims(String token, Function<Claims, T>claimsResolver)
	{
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	//this will return claim from token
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
	}
	//to check token is expired till now or not
	private Boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}
	//for validating the token
	
	public Boolean validateToken(String token, UserDetails userDetails)
	{
		final String userName = extractUsername(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String genrateToken(String userName, String role)
	{
		Map<String, Object>claims= new HashMap<>();
		claims.put("role", role);
		return createToken(claims, userName);
	}
	
	private String createToken(Map<String,Object>claims, String subject)
	{
		return Jwts.builder().setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000* 60 * 60 *10))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}
}
