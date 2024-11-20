package com.simplogics.base.service;

import com.simplogics.base.dto.PaginatedResponse;
import com.simplogics.base.dto.RegisterUserDto;
import com.simplogics.base.entity.Role;
import com.simplogics.base.entity.User;
import com.simplogics.base.exception.PawException;

import com.simplogics.base.dto.UserResponse;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface IUserService {

	UserResponse findById(long id) throws PawException;

	User findUserByUsername(String username) throws PawException;

	void updatePassword(String email, String encodedPassword) throws PawException;

	void createDefaultUser(String encodedPassword, Role role);

	void createDefaultUsers(String encodedPassword, Role role);

	PaginatedResponse<UserResponse> getUsers(String search, List<Long> roleIds, Boolean isActive, Long pageSize, Long pageNumber, Sort sort);

	UserResponse registerUser(RegisterUserDto registerUserDto, Role role) throws PawException;

//	UserResponse registerUser(RegisterUserDto registerUserDto) throws PawException;
}
