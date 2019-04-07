package com.gbs.mr.weather;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class MyPartitioner extends HashPartitioner<MyKey,DoubleWritable>{

	@Override
	public int getPartition(MyKey key, DoubleWritable value, int numReduceTasks) {
		return (key.getYear() - 1949) % numReduceTasks;
	}
}
