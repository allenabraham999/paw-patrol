package com.simplogics.base.security.utils;

import com.simplogics.base.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PasswordResetTokenUtil {

	
	@Value("${password.reset.key}")
	private String passwordResetKey;

	@Value("${key.expiration.time}")
	private String expirationTime;

	public String generateResetToken(String email) {

		return Jwts.builder()
				.claims()
				.subject(email)
				.expiration(new Date(new Date().getTime() + Long.parseLong(expirationTime))).and()
				.signWith(Utils.getSignInKey(passwordResetKey))
				.compact();
	}

	public boolean isTokenExpired(String token) {
		return decodeResetToken(token).getExpiration().before(new Date());
	}

	public String getSubject(String token){
		return decodeResetToken(token).getSubject();
	}

	public Claims decodeResetToken(String token) {
		return Jwts.parser()
				.verifyWith(Utils.getSignInKey(passwordResetKey))
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

}
