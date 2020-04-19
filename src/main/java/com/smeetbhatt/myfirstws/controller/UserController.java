package com.smeetbhatt.myfirstws.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.smeetbhatt.myfirstws.model.request.UserDetailsRequestBody;
import com.smeetbhatt.myfirstws.model.response.UserRest;
import com.smeetbhatt.myfirstws.service.UserService;
import com.smeetbhatt.myfirstws.shared.dto.UserDto;

@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	UserService userService;

	
	@GetMapping(path="/{userId}")
	public UserRest getUser(@PathVariable String userId) {
		System.out.println("Getting user");
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}
		
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestBody userDetails) {

		System.out.println("Creating user");
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);
		
		return returnValue;
	}

	@PutMapping
	public String updateUser() {
		System.out.println("Updating user");
		return "update user was called";
	}
	
	@DeleteMapping
	public String deleteUser() {
		System.out.println("Deletng user");
		return "delete user was called";
	}
}
