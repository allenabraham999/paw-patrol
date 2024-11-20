package com.simplogics.base.service;

import com.simplogics.base.dto.PaginatedResponse;
import com.simplogics.base.dto.RegisterUserDto;
import com.simplogics.base.entity.Role;
import com.simplogics.base.entity.User;
import com.simplogics.base.enums.UserRole;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.mapper.PaginationMapper;
import com.simplogics.base.mapper.UserMapper;
import com.simplogics.base.specifications.UserSpecification;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.simplogics.base.dto.UserResponse;
import com.simplogics.base.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	private Log logger = LogFactory.getLog(UserService.class);
	@Override
	public UserResponse findById(long id) throws PawException {
		User user = userRepository.findById(id).orElseThrow(()->
				new PawException("user.id.not.found", HttpStatus.BAD_REQUEST));
		return UserMapper.convertEntityToDto(user);
	}

	@Override
	public User findUserByUsername(String username) throws PawException {
		return userRepository.findByEmail(username).orElseThrow(()->
				new PawException("user.email.not.found", HttpStatus.BAD_REQUEST));
	}

	@Override
	public void updatePassword(String email, String encodedPassword) throws PawException {
		User user = findUserByUsername(email);
		user.setPassword(encodedPassword);
		userRepository.save(user);
	}

	@Override
	public void createDefaultUser(String encodedPassword, Role role) {
		if (userRepository.count() == 0) {
			List<Role> roles = new ArrayList<>();
			roles.add(role);

			User defaultUser = new User();
			defaultUser.setName("Super Admin Framework");
			defaultUser.setEmail("super.admin@simplogics.com");
			defaultUser.setPassword(encodedPassword);
			defaultUser.setUserRoles(roles);
			userRepository.save(defaultUser);
		}
	}

	@Override
	public void createDefaultUsers(String encodedPassword, Role role) {
		if (userRepository.count() == 0) {
			List<Role> roles = new ArrayList<>();
			roles.add(role);
			List<User> users = new ArrayList<>();
			User defaultUser1 = new User();
			defaultUser1.setName("Super Admin Framework");
			defaultUser1.setEmail("admin@simplogics.com");
			defaultUser1.setPassword(encodedPassword);
			defaultUser1.setUserRoles(roles);

			User defaultUser2 = new User();
			defaultUser2.setName("Super Admin Framework");
			defaultUser2.setEmail("dogrescue@simplogics.com");
			defaultUser2.setPassword(encodedPassword);
			defaultUser2.setUserRoles(roles);

			User defaultUser3 = new User();
			defaultUser3.setName("Super Admin Framework");
			defaultUser3.setEmail("dogwelfare@simplogics.com");
			defaultUser3.setPassword(encodedPassword);
			defaultUser3.setUserRoles(roles);
			users.add(defaultUser1);
			users.add(defaultUser2);
			users.add(defaultUser3);
			userRepository.saveAll(users);
		}

	}

	@Override
	public PaginatedResponse<UserResponse> getUsers(String search, List<Long> roleIds, Boolean isActive, Long pageSize, Long pageNumber, Sort sort) {
		Specification<User> spec = UserSpecification.filterUsers(search, roleIds, isActive);
		Page<User> users = userRepository.findAll(spec, PageRequest.of(pageNumber.intValue(), pageSize.intValue(), sort));
		List<UserResponse> responseList = users.stream().map(UserMapper::convertEntityToDto).toList();
		return PaginationMapper.convertEntityToResponse(users, responseList);
	}

	@Override
	public UserResponse registerUser(RegisterUserDto registerUserDto, Role role) throws PawException {
//		UserRole role = null;
//		try{
//			UserRole.valueOf(registerUserDto.getRole());
//		}catch (IllegalArgumentException e){
//			logger.info("Illegal role");
//			logger.info(e.getMessage());
//		}
		User existingUser = userRepository.findByEmail(registerUserDto.getEmail()).orElse(null);
		if(existingUser!=null){
			throw new PawException("Email already exists!", HttpStatus.BAD_REQUEST);
		}
		if(role.equals(UserRole.ROLE_ADMIN)){
			throw new PawException("admin.registration.prohibited", HttpStatus.BAD_REQUEST);
		}
		registerUserDto.setEmail(registerUserDto.getEmail().toLowerCase());
		User user = UserMapper.convertRegistrationDetialsToEntity(registerUserDto);
		List<Role> userRoles = new ArrayList<>();
		userRoles.add(role);
		user.setUserRoles(userRoles);
		User userResponse = userRepository.save(user);
		return UserMapper.convertEntityToDto(userResponse);
	}
}
