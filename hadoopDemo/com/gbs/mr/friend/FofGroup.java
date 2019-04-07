package com.gbs.mr.friend;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FofGroup extends WritableComparator{

	public FofGroup() {
		super(User.class,true);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		User u1 = (User) a;
		User u2 = (User) b;
		return u1.getName().compareTo(u2.getName());
	}
	
}
