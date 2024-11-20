package com.simplogics.base.security.utils;

import com.simplogics.base.entity.User;
import com.simplogics.base.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil{

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private String jwtExpirationMs;

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	public String generateJwtToken(User user) {
		Map<String,Object> claims = new HashMap<>();
		claims.put("email",user.getEmail());
		claims.put("userId",user.getId());
		return Jwts.builder()
				.claims()
				.add(claims)
				.issuedAt(new Date())
				.subject(String.valueOf(user.getId()))
				.expiration(new Date(new Date().getTime() + Long.parseLong(jwtExpirationMs))).and()
				.signWith(Utils.getSignInKey(jwtSecret))
				.compact();
	}



	public String getUserNameFromJwtToken(String token) {
		return String.valueOf(Jwts.parser().verifyWith(Utils.getSignInKey(jwtSecret)).build().parseSignedClaims(token).getPayload().get("email"));
	}

	public  boolean validateJwtToken(String authToken) {
		if(authToken == null){
			logger.error("No JWT is provided");
			return false;
		}else{
			Jwts.parser().verifyWith(Utils.getSignInKey(jwtSecret)).build().parseSignedClaims(authToken);
			return true;
		}
	}
		
}
