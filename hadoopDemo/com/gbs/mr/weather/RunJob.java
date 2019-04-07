package com.gbs.mr.weather;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
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
		conf.set("mapreduce.job.jar", "weather.jar");
		try {
			FileSystem fs = FileSystem.get(conf);
			
			Job job = Job.getInstance(conf);
			job.setJarByClass(RunJob.class);
			job.setJobName("weather");
			
			
			job.setMapperClass(WeatherMapper.class);
			job.setReducerClass(WeatherReducer.class);
			
			job.setMapOutputKeyClass(MyKey.class);
			job.setMapOutputValueClass(DoubleWritable.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(NullWritable.class);
			
			job.setPartitionerClass(MyPartitioner.class);
			job.setSortComparatorClass(MySort.class);
			job.setGroupingComparatorClass(MyGroup.class);
			
			job.setNumReduceTasks(3);
			
			job.setInputFormatClass(KeyValueTextInputFormat.class);
			
			FileInputFormat.addInputPath(job, new Path("/in/weather/"));
			
			Path outPath = new Path("/out/weather/");
			
			if(fs.exists(outPath)) {
				fs.delete(outPath, true);
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
	
	static class WeatherMapper extends Mapper<Text, Text, MyKey, DoubleWritable>{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		NullWritable v = NullWritable.get();
		@Override
		protected void map(Text key, Text value, Mapper<Text, Text, MyKey, DoubleWritable>.Context context)
				throws IOException, InterruptedException {
			try {
				Date date = sdf.parse(key.toString());
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				
				double hot = Double.parseDouble(value.toString().substring(0, value.toString().lastIndexOf("c")));
				
				MyKey k = new MyKey();
				k.setYear(year);
				k.setMonth(month);
				k.setHot(hot);
				
				context.write(k, new DoubleWritable(hot));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class WeatherReducer extends Reducer<MyKey, DoubleWritable, Text, NullWritable>{
		@Override
		protected void reduce(MyKey key, Iterable<DoubleWritable> values,
				Reducer<MyKey, DoubleWritable, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			int i = 0;
			for(DoubleWritable v : values) {
				i ++;
				String msg = key.getYear()+"\t"+key.getMonth()+"\t"+v.get();
				context.write(new Text(msg), NullWritable.get());
				if(i == 3) {
					break;
				}
			}
		}
	}
}
