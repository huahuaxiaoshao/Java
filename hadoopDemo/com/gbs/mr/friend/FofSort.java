package com.gbs.mr.friend;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FofSort extends WritableComparator{

	public FofSort() {
		super(User.class,true);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		User u1 = (User)a;
		User u2 = (User)b;
		
		int result = u1.getName().compareTo(u2.getName());
		if(result == 0) {
			return -Integer.compare(u1.getFriendsCount(), u2.getFriendsCount());
		}else {
			return result;
		}
	}
}
