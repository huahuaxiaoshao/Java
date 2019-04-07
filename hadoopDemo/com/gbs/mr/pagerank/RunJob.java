package com.gbs.mr.pagerank;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RunJob {

	public static enum MyCounter{
		my
	}
	
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.set("mapreduce.job.jar", "pr.jar");
		double d = 0.001;
		int i = 0;
		while(true) {
			i ++;
			try {
				conf.setInt("runCount", i);
				FileSystem fs = FileSystem.get(conf);
				Job job = Job.getInstance(conf);
				
				job.setJarByClass(RunJob.class);
				job.setMapperClass(PageRankMapper.class);
				job.setReducerClass(PageRankReducer.class);
				
				job.setMapOutputKeyClass(Text.class);
				job.setMapOutputValueClass(Text.class);
				
				job.setOutputKeyClass(Text.class);
				job.setOutputValueClass(Text.class);
				
				job.setInputFormatClass(KeyValueTextInputFormat.class);
				
				if(i == 1) {
					FileInputFormat.addInputPath(job, new Path("/in/pr/"));
				}else {
					FileInputFormat.addInputPath(job, new Path("/out/pr/pr"+(i-1)));
				}
				
				Path outPath = new Path("/out/pr/pr" + i);
				if(fs.exists(outPath)) {
					fs.delete(outPath,true);
				}
				
				FileOutputFormat.setOutputPath(job, outPath);
				if(job.waitForCompletion(true)) {
					System.out.println("job complete");
					long sum = job.getCounters().findCounter(MyCounter.my).getValue();
					System.out.println(sum);
					double avg = sum / 4000.0;
					if(avg < d) {
						break;
					}
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
	
	static class PageRankMapper extends Mapper<Text, Text, Text, Text>{
		@Override
		protected void map(Text key, Text value, Mapper<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			
			Node node = null;
			
			int runCount = context.getConfiguration().getInt("runCount", 1);
			
			if(runCount == 1) {
				node = Node.fromMR("1.0"+"\t"+value.toString());
			}else {
				node = Node.fromMR(value.toString());
			}
			
			//A:1.0	B	D
			context.write(key, new Text(node.toString()));
			
			if(node.containsAdjacentNodeNames()) {
				double outVaule = node.getPageRank() / node.getAdjacentNodeNames().length;
				for(String outPage : node.getAdjacentNodeNames()) {
					//B:0.5  D:0.5
					context.write(new Text(outPage), new Text(outVaule + ""));
				}
			}
			
		}
	}
	
	static class PageRankReducer extends Reducer<Text, Text, Text, Text>{
		@Override
		protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			double sum = 0.0;
			Node sourceNode = null;
			for(Text t : values) {
				Node node = Node.fromMR(t.toString());
				if(node.containsAdjacentNodeNames()) {
					sourceNode = node;
				}else {
					sum += node.getPageRank();
				}
			}
			double newPR = (0.15 / 4) + (0.85 * sum);
			System.out.println("*********** new pageRank value is "+newPR);
			
			//把新的pr值和计算之前的pr比较
			double d = newPR - sourceNode.getPageRank();
			
			int j = (int)(d * 1000.0);
			j = Math.abs(j);
			System.out.println(j + "___________");
			context.getCounter(MyCounter.my).increment(j);
			
			sourceNode.setPageRank(newPR);
			context.write(key, new Text(sourceNode.toString()));
		}
	}
}
