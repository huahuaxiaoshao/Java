package com.gbs.mr.friend;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class User implements WritableComparable<User>{

	private String name;
	private int friendsCount;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getFriendsCount() {
		return friendsCount;
	}
	
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public User() {
		
	}

	public User(String name, int friendsCount) {
		this.name = name;
		this.friendsCount = friendsCount;
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(name);
		out.writeInt(friendsCount);
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		this.name = in.readUTF();
		this.friendsCount = in.readInt();
	}
	
	@Override
	public int compareTo(User o) {
		int result = this.name.compareTo(o.getName());
		if(result == 0) {
			return Integer.compare(this.getFriendsCount(), o.getFriendsCount());
		}
		return result;
	}
}
