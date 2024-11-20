package com.simplogics.base.controller;


import com.simplogics.base.dto.ForgetPasswordRequest;
import com.simplogics.base.dto.BaseResponse;
import com.simplogics.base.dto.LoginResponse;
import com.simplogics.base.dto.RegisterUserDto;
import com.simplogics.base.dto.ResetPasswordRequest;
import com.simplogics.base.dto.UserResponse;
import com.simplogics.base.entity.Role;
import com.simplogics.base.entity.User;
import com.simplogics.base.enums.UserRole;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.service.IPasswordService;
import com.simplogics.base.service.IRoleService;
import com.simplogics.base.service.IUserService;
import com.simplogics.base.utils.ApiRoutes;
import com.simplogics.base.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.simplogics.base.annotation.APIResult;
import com.simplogics.base.service.IAuthService;
import com.simplogics.base.dto.LoginRequest;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(ApiRoutes.AUTH)
public class AuthController {

	@Autowired
	private IAuthService authService;

	@Autowired
	private IUserService userService;

	@Autowired
	IRoleService roleService;
	@Autowired
	private IPasswordService passwordService;
	
	@PostMapping(value = ApiRoutes.LOGIN)
	@APIResult(error_message = "user.auth.fail")
	public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest loginDto) throws  Exception {
		User user = userService.findUserByUsername(loginDto.getEmail());
		LoginResponse response = authService.login(loginDto, user);
		return ResponseUtil.getStatusOkResponseEntity(response, "user.auth.success", 1);
	}

	@PostMapping(value = ApiRoutes.FORGOT_PASSWORD)
	@APIResult(error_message = "forgot.password.fail")
	public ResponseEntity<BaseResponse> forgotPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) throws  Exception {
		User user = userService.findUserByUsername(forgetPasswordRequest.getEmail());
		authService.forgetPassword(user);
		return ResponseUtil.getStatusOkResponseEntity(null, "forgot.password.success", 1);
	}

	@RequestMapping(value = ApiRoutes.RESET_PASSWORD, method = RequestMethod.POST)
	@APIResult(error_message = "reset.password.fail")
	public ResponseEntity<BaseResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) throws  Exception {
		String email = authService.validateTokenAndGetSubject(resetPasswordRequest.getToken());
//		String encodedPassword = passwordService.validateAndGenerateEncodedPassword(resetPasswordRequest.getNewPassword());
		userService.updatePassword(email, "encodedPassword");
		return ResponseUtil.getStatusOkResponseEntity(null, "reset.password.success", 1);
	}

	@RequestMapping(value = ApiRoutes.REGISTER, method = RequestMethod.POST)
	@APIResult(error_message = "reset.password.fail")
	public ResponseEntity<BaseResponse> register(@Valid @RequestBody RegisterUserDto registerUserDto) throws  Exception {
		Role role = roleService.findByRole(UserRole.ROLE_CLIENT);
		List<Role> roles = Collections.singletonList(role);
//		User userResponse = userService.findUserByUsername(registerUserDto.getEmail());
//		if(userResponse!=null){
//			throw new PawException("Already exists", HttpStatus.BAD_REQUEST);
//		}
//		UserRole role = UserRole.valueOf(registerUserDto.getRole());
//		roleService.findByRole(registerUserDto.getRole());
		registerUserDto.setEmail(registerUserDto.getEmail().toLowerCase());
		registerUserDto.setPassword(passwordService.validateAndGenerateEncodedPassword(registerUserDto.getPassword()));
		UserResponse response = userService.registerUser(registerUserDto, role);
		return ResponseUtil.getStatusOkResponseEntity(response, "client.creation.success", 1);
	}
}
