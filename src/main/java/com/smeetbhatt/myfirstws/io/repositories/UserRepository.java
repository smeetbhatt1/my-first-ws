package com.smeetbhatt.myfirstws.io.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.smeetbhatt.myfirstws.io.entity.UserEntity;

@Repository
//public interface UserRepository extends CrudRepository<UserEntity, Long> {
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	
}
