package com.smeetbhatt.myfirstws.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.smeetbhatt.myfirstws.exceptions.UserServiceException;
import com.smeetbhatt.myfirstws.model.request.UserDetailsRequestBody;
import com.smeetbhatt.myfirstws.model.response.ErrorMessages;
import com.smeetbhatt.myfirstws.model.response.OperationalStatusModel;
import com.smeetbhatt.myfirstws.model.response.RequestOpeartionName;
import com.smeetbhatt.myfirstws.model.response.RequestOperationStatus;
import com.smeetbhatt.myfirstws.model.response.UserRest;
import com.smeetbhatt.myfirstws.service.UserService;
import com.smeetbhatt.myfirstws.shared.dto.UserDto;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String userId) {
		System.out.println("Getting user");
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, // accepts
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE } // returns
	)
	public UserRest createUser(@RequestBody UserDetailsRequestBody userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		// throw new NullPointerException("First name is empty");

		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);

		return returnValue;
	}

	@PutMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, // accepts
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE } // returns
	)
	public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestBody userDetails) {
		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		// throw new NullPointerException("First name is empty");

		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updateUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationalStatusModel deleteUser(@PathVariable String userId) {
		OperationalStatusModel returnValue = new OperationalStatusModel();
		
		userService.deleteUser(userId);
		
		returnValue.setOperationName(RequestOpeartionName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public List<UserRest> getUsers(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit
			) {
		List<UserRest> returnValueList = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		for(UserDto userDto : users) {
			UserRest userRest = new UserRest();
			BeanUtils.copyProperties(userDto, userRest);
			returnValueList.add(userRest);
		}
		return returnValueList;
	}
}
