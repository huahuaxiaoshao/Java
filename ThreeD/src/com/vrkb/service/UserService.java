package com.vrkb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vrkb.bean.User;
import com.vrkb.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public User getPassword(String userEmail){
		User user = new User();
		try {
			user = userMapper.getPassword(userEmail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public List<User> getOrdinaryUsers(){
		List<User> users = new ArrayList<User>();
		try {
			users = userMapper.getOrdinaryUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public boolean save(User user){
		boolean flag = false;
		try {
			flag = userMapper.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean updateGrade(User user){
		boolean flag = false;
		try {
			flag = userMapper.updateGrade(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
