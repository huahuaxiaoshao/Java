package com.gbs.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HdfsTest {
	//初始化连接配置，并且除去classpath目录中的所有文件
	private Configuration conf = new Configuration();
	private FileSystem fs;
	
	@Before
	public void setup()throws IOException {
		fs = FileSystem.get(conf);
	}
	
	@After
	public void end()throws Exception {
		if(fs != null) {
			fs.close();
		}
	}
	
	@Test
	//在hdfs上创建目录
	public void mkdir() throws IOException {
		Path path = new Path("/test/gbs");
		fs.mkdirs(path);
	}
	
	@Test
	//上传文件到hdfs
	public void upload() throws IOException {
		Path path = new Path("/in/friend/friend.txt");
		FSDataOutputStream out = fs.create(path);
		IOUtils.copyBytes(new FileInputStream("D:\\BaiduYunDownload\\尚学堂大数据\\尚学堂大数据周末班原版不加密\\2016-01-24-hadoop3\\下午第三节课\\mapreducer1\\data\\friend"), out, conf);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	//将多个小文件组成一个小文件
	public void smallFile() throws IllegalArgumentException, IOException {
		SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, new Path("/test/gbs/test.seq"), Text.class, Text.class, CompressionType.NONE);
		File dir = new File("E:/hdfs");
		for(File file:dir.listFiles()) {
			writer.append(new Text(file.getName()), new Text(FileUtils.readFileToString(file)));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	//从hdfs读数据
	public void readSmallFile() throws IllegalArgumentException, IOException {
		SequenceFile.Reader reader = new Reader(fs, new Path("/test/gbs/test2.seq"),conf);
		Text value = new Text();
		while(reader.next(new Text("NOTICE.txt"),value)) {
			System.out.println(value.toString());
		}
	}
	
	@Test
	//从hdfs中删除文件或者目录
	public void deleteFile() throws IllegalArgumentException, IOException {
		fs.delete(new Path("/test/gbs/test.seq"), false);
	}
	
}
