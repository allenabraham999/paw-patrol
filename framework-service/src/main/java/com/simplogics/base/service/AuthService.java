package com.simplogics.base.service;

import com.simplogics.base.config.SecurityConfig;
import com.simplogics.base.dto.LoginResponse;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.security.utils.JwtTokenUtil;
import com.simplogics.base.security.utils.PasswordResetTokenUtil;
import com.simplogics.base.utils.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.stereotype.Service;

import com.simplogics.base.dto.LoginRequest;
import com.simplogics.base.entity.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService implements IAuthService {

	@Autowired
	SecurityConfig securityConfig;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	PasswordResetTokenUtil passwordResetTokenUtil;

	@Autowired
	IEmailService emailService;

	@Value("${domain.name}")
	private String domainURL;

	public static final String RESET_PASSWORD_PATH = "/reset-password/";


	@Override
	public LoginResponse login(LoginRequest loginDto, User user) throws Exception {
		if(!user.getIsActive())
			throw new PawException("user.not.active", HttpStatus.BAD_REQUEST);
		AuthenticationManager authenticationManager = securityConfig.authenticationManager(new AuthenticationConfiguration());
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		return LoginResponse.builder()
				.token(jwtTokenUtil.generateJwtToken(user))
				.build();
	}

	@Override
	public void forgetPassword(User user) throws PawException {
		if(!user.getIsActive())
			throw new PawException("user.not.active", HttpStatus.BAD_REQUEST);
		String resetPasswordToken = passwordResetTokenUtil.generateResetToken(user.getEmail());
		String resetUrl = generateResetPasswordUrl(domainURL, resetPasswordToken);
		generateAndSendEmail(user, resetUrl);
	}

	@Override
	public String validateTokenAndGetSubject(String token) throws PawException {
		if(passwordResetTokenUtil.isTokenExpired(token)){
			throw new PawException("reset.password.token.expired", HttpStatus.BAD_REQUEST);
		}
		return passwordResetTokenUtil.getSubject(token);
	}

	private void generateAndSendEmail(User user, String resetUrl) throws PawException {
		Map<String, Object> mailVariables = new HashMap<>();
		mailVariables.put("name", user.getName());
		mailVariables.put("resetTokenUrl", resetUrl);
		emailService.sendHtmlEmail(user.getEmail(),
				Translator.translateToLocale("forget.password.email.subject"),
				Translator.translateToLocale("forget.password.email.template"),
				mailVariables);
	}

	private String generateResetPasswordUrl(String domainURL, String resetPasswordToken) {
		return String.format("%s%s%s", domainURL, RESET_PASSWORD_PATH, resetPasswordToken);
	}

}
