package com.vrkb.mapper;

import java.util.List;

import com.vrkb.bean.User;

public interface UserMapper {

	public User getPassword(String userEmail);
	
	public List<User> getOrdinaryUsers();
	
	public boolean updateGrade(User user);
	
	public boolean save(User user);
}
