package com.smeetbhatt.myfirstws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smeetbhatt.myfirstws.exceptions.UserServiceException;
import com.smeetbhatt.myfirstws.io.entity.UserEntity;
import com.smeetbhatt.myfirstws.io.repositories.UserRepository;
import com.smeetbhatt.myfirstws.model.response.ErrorMessages;
import com.smeetbhatt.myfirstws.service.UserService;
import com.smeetbhatt.myfirstws.shared.Utils;
import com.smeetbhatt.myfirstws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Record already exists");
		}
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnedValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnedValue);
		
		return returnedValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		UserDto returnedValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnedValue);
		
		return returnedValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnedValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		System.out.println(userEntity.toString());
		System.out.println("Smeet");
	
		if (userEntity == null)
			throw new UsernameNotFoundException(userId);
		
		BeanUtils.copyProperties(userEntity, returnedValue);
		return returnedValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserDto returnedValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity updatedUser =  userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUser, returnedValue);
		return returnedValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userRepository.delete(userEntity);
		
		
	}

	
}