package com.simplogics.base.service;

import com.simplogics.base.dto.LoginResponse;
import com.simplogics.base.entity.User;
import com.simplogics.base.exception.PawException;
import org.springframework.stereotype.Service;

import com.simplogics.base.dto.LoginRequest;


@Service
public interface IAuthService {

	LoginResponse login(LoginRequest loginDto, User user) throws Exception;

	void forgetPassword(User user) throws PawException;

	String validateTokenAndGetSubject(String token) throws PawException;
}
