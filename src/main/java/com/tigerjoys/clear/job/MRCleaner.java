package com.tigerjoys.clear.job;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MRCleaner {

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, Text> {
		// private final static IntWritable one = new IntWritable(1);
		// private Text word = new Text();
		//
		// public void map(Object key,Text value,Context context) throws
		// IOException, InterruptedException{
		// StringTokenizer itr = new StringTokenizer(value.toString());
		// while(itr.hasMoreTokens()){
		// word.set(itr.nextToken());
		// context.write(word, one);
		// }
		// }
		//
		// memeber variable
		private Logger logger = LogManager.getLogger();
		private Cleaner deliver_log = new Cleaner();

		// clean the deliver log
		public void map(Object key, Text value, Context context) {
			String result = deliver_log.clean(value.toString());//
			try {
				context.write((Text) key, new Text(result));
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}

		}
	}

	public static class LineReducer extends
			Reducer<Text, Text, Text, Text> {
		// private IntWritable result = new IntWritable();
		// public void reduce(Text key,Iterable<IntWritable> values, Context
		// context) throws IOException, InterruptedException{
		// int sum = 0;
		// for (IntWritable val : values) {
		// sum += val.get();
		// }
		// result.set(sum);
		// context.write(key, result);
		// }
		//
		private Logger logger = LogManager.getLogger();
		public void reduce(Text key,Text result,Context context) {
			try {
				context.write(new Text(""), result);
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}

		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "DeliverLogCleaner");
		job.setJarByClass(MRCleaner.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(LineReducer.class);
		job.setReducerClass(LineReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
