package com.gbs.mr.friend;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RunJob {
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.set("mapreduce.job.jar", "friend.jar");
		if(run1(conf)) {
			run2(conf);
		}
	}
	
	private static void run2(Configuration conf) {
		try {
			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance(conf);
			job.setJobName("f2");
			
			job.setJarByClass(RunJob.class);
			job.setMapperClass(SortMapper.class);
			job.setReducerClass(SortReducer.class);
			
			job.setSortComparatorClass(FofSort.class);
			job.setGroupingComparatorClass(FofGroup.class);
			
			job.setMapOutputKeyClass(User.class);
			job.setMapOutputValueClass(User.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
			job.setInputFormatClass(KeyValueTextInputFormat.class);
			
			FileInputFormat.addInputPath(job, new Path("/out/friend/f1/"));
			
			Path outPath = new Path("/out/friend/f2/");
			
			if(fs.exists(outPath)) {
				fs.delete(outPath,true);
			}
			
			FileOutputFormat.setOutputPath(job, outPath);
			
			if(job.waitForCompletion(true)) {
				System.out.println("job complete");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static boolean run1(Configuration conf) {
		try {
			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance(conf);
			job.setJobName("f1");
			
			job.setJarByClass(RunJob.class);
			job.setMapperClass(FofMapper.class);
			job.setReducerClass(FofReducer.class);
			
			job.setMapOutputKeyClass(Fof.class);
			job.setMapOutputValueClass(IntWritable.class);
			
			job.setOutputKeyClass(Fof.class);
			job.setOutputValueClass(IntWritable.class);
			
			job.setInputFormatClass(KeyValueTextInputFormat.class);
			
			FileInputFormat.addInputPath(job, new Path("/in/friend/"));
			
			Path outPath = new Path("/out/friend/f1/");
			
			if(fs.exists(outPath)) {
				fs.delete(outPath,true);
			}
			
			FileOutputFormat.setOutputPath(job, outPath);
			
			return job.waitForCompletion(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	static class FofMapper extends Mapper<Text, Text, Fof, IntWritable>{
		@Override
		protected void map(Text key, Text value, Mapper<Text, Text, Fof, IntWritable>.Context context)
				throws IOException, InterruptedException {
			String user = key.toString();
			String[] friends = StringUtils.split(value.toString(),'\t');
			for(int i = 0;i < friends.length - 1;i ++) {
				Fof f1 = new Fof(user, friends[i]);
				context.write(f1, new IntWritable(0));
				for(int j = i + 1;j < friends.length;j ++) {
					Fof f2 = new Fof(friends[i], friends[j]);
					context.write(f2, new IntWritable(1));
				}
			}
		}
	}
	
	static class FofReducer extends Reducer<Fof, IntWritable, Fof, IntWritable>{
		@Override
		protected void reduce(Fof key, Iterable<IntWritable> values, Reducer<Fof, IntWritable, Fof, IntWritable>.Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			boolean flag = true;
			for (IntWritable i : values) {
				if(i.get() == 0) {
					flag = false;
					break;
				}else {
					sum += i.get();
				}
			}
			
			if(flag) {
				context.write(key, new IntWritable(sum));
			}
		}
	}
	
	static class SortMapper extends Mapper<Text, Text, User, User>{
		@Override
		protected void map(Text key, Text value, Mapper<Text, Text, User, User>.Context context)
				throws IOException, InterruptedException {
			String[] parts = StringUtils.split(value.toString(), '\t');
			String other = parts[0];
			int friendsCount = Integer.parseInt(parts[1]);
			context.write(new User(key.toString(), friendsCount), new User(other, friendsCount));
			context.write(new User(other, friendsCount), new User(key.toString(), friendsCount));
		}
	}
	
	static class SortReducer extends Reducer<User, User, Text, Text>{
		@Override
		protected void reduce(User key, Iterable<User> values, Reducer<User, User, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String user = key.getName();
			StringBuffer sb = new StringBuffer();
			for (User u : values) {
				sb.append(u.getName() + ":" + u.getFriendsCount());
				sb.append(",");
			}
			context.write(new Text(user), new Text(sb.toString()));
		}
	}
}
