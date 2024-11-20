package com.simplogics.base.controller;

import com.simplogics.base.annotation.APIResult;
import com.simplogics.base.dto.BaseResponse;
import com.simplogics.base.dto.PaginatedResponse;
import com.simplogics.base.dto.RegisterUserDto;
import com.simplogics.base.dto.ResetPasswordRequest;
import com.simplogics.base.dto.UserResponse;
import com.simplogics.base.entity.Role;
import com.simplogics.base.entity.User;
import com.simplogics.base.enums.UserRole;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.security.authDetails.UserAuthDetails;
import com.simplogics.base.service.IRoleService;
import com.simplogics.base.service.PasswordService;
import com.simplogics.base.utils.ApiRoutes;
import com.simplogics.base.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simplogics.base.service.IUserService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(ApiRoutes.USER)
public class UserController extends BaseController{

	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	PasswordService passwordService;
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiResponse(content = @Content(schema = @Schema(implementation = PaginatedResponse.class)))
	@APIResult(error_message = "user.fetch.fail")
	public ResponseEntity<BaseResponse> getUsers(@RequestParam(required = false, name = "search") String search,
												 @RequestParam(required = false, name = "isActive") Boolean isActive,
												 @RequestParam(required = false, name = "roleIds") List<Long> roleIds,
												 @RequestParam(required = false, name = "sortBy") String sortBy,
												 @RequestParam(required = false, name = "sortDir") String sortDir,
												 @RequestParam(required = false, name = "pageSize", defaultValue = "10") Long pageSize,
												 @RequestParam(required = false, name = "pageNumber", defaultValue = "0") Long pageNumber) {
		Sort sort = Sort.by(Sort.Order.by(sortBy != null ? sortBy : "id").with(Sort.Direction.fromString(sortDir != null ? sortDir : "DESC")));
		PaginatedResponse<UserResponse> response = userService.getUsers(search, roleIds, isActive, pageSize, pageNumber, sort);
		return ResponseUtil.getStatusOkResponseEntity(response, "user.fetch.success", 0);
	}

	@GetMapping(path = ApiRoutes.CURRENT)
	public ResponseEntity<BaseResponse> getCurrentUser() throws PawException {
		UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
		UserResponse user = userService.findById(userAuthDetails.getId());
		return ResponseUtil.getStatusOkResponseEntity(user, "user.fetch.success", 1);
	}


}
