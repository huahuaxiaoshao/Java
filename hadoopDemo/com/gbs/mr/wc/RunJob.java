package com.gbs.mr.wc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RunJob {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.set("mapreduce.job.jar","wc.jar"); 
		try {
			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance(conf);
			job.setJarByClass(RunJob.class);
			
			job.setJobName("wc");
			
			job.setMapperClass(WordCountMapper.class);
			job.setReducerClass(WordCountReducer.class);
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(IntWritable.class);
			
			FileInputFormat.addInputPath(job, new Path("/in/wc2/"));
			
			Path outPath = new Path("/out/wc2/");
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
}
