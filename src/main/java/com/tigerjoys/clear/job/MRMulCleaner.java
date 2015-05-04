package com.tigerjoys.clear.job;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tigerjoys.clear.config.Config;

public class MRMulCleaner {
	private static Logger logger = LogManager.getLogger();
	
	
	
	public static class LogTokenizerMapper extends Mapper<Object, Text, Text, Text>{
		
		private Logger logger = LogManager.getLogger();
		private Cleaner deliver_log = new Cleaner();
	
		
		public void map(Object key, Text value, Context context) {
			
		String result = "NULL";
		if (value != null) {
			if (StringUtils.isNotEmpty(value.toString().trim())) {
				result = deliver_log.clean(value.toString());//value不为null，并且内容不为空
			} else {
				result = "NULL" + Config.INFO + "NULL";
				logger.error("输入日志Text对象value实例不为null,但是日志内容为空");
			}
		} else {
			result = "NULL" + Config.INFO + "NULL";
			logger.error("输入日志Text对象实例value为null");
		}
		try {
			//对result进行分解。
			String[] key_value = result.split(Config.INFO);//把错误信息直接输出到mapReduce中去。
			context.write(new Text(key_value[Config.KEY_INDEX]), new Text(key_value[Config.VALUE_INDEX]));
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
			
			
			
		}//map()--method
		
	}//LogTokenizerMapper--class
	
	
	public static class LogReducer extends Reducer<Text, Text, Text, Text>{
		private Logger logger = LogManager.getLogger();
		private Cleaner deliver_log = new Cleaner();
		
		private MultipleOutputs  mos;
		
		public void setup(Context context){
			mos = new MultipleOutputs(context);
		}//setup--method
		public void reduce(Text key, Iterable<Text> values, Context context) {
			
			if (key.toString().equals(Config.IN_COLON)) {
				for (Text value : values) {
					try {
						mos.write(Config.IN_COLON, NullWritable.class, value);
					} catch (IOException e) {
						logger.error(e.getMessage());
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
			}
			if (key.toString().equals(Config.OUT_COLON)) {
				for (Text value : values) {
					try {
						mos.write(Config.OUT_COLON, NullWritable.class, value);
					} catch (IOException e) {
						logger.error(e.getMessage());
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
			}
			if (key.toString().equals(Config.COMBINE_COLON)) {
				for (Text value : values) {
					try {
						mos.write(Config.COMBINE_COLON, NullWritable.class, value);
					} catch (IOException e) {
						logger.error(e.getMessage());
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
			}
			if (key.toString().equals("NULL")) {
				for (Text value : values) {
					try {
						mos.write("NULL", NullWritable.class, value);
					} catch (IOException e) {
						logger.error(e.getMessage());
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
			}
			
			
			
			
		}//reduce--method
		public void cleanup(Context context) {
			try {
				mos.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}//cleanup--method
	}//LogReducer--class
	
	
	
	public static void main(String[] args){

		Configuration conf = new Configuration();
		Job job = null;
		
		try {
			job = Job.getInstance(conf, "多输出清洗");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		job.setJarByClass(MRMulCleaner.class);
		job.setMapperClass(LogTokenizerMapper.class);
		job.setCombinerClass(LogReducer.class);
		job.setReducerClass(LogReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		

		try {
			FileInputFormat.setInputPaths(job, new Path(args[0]));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		MultipleOutputs.addNamedOutput(job, Config.IN_COLON, TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, Config.OUT_COLON, TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, Config.COMBINE_COLON, TextOutputFormat.class, NullWritable.class, Text.class);
		
		
	}//main--method
	
	
	

}


//package com.tigerjoys.clear.job;
//
//import java.io.IOException;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//
//public class MRCleanerV270 {
//
//	public static class TokenizerMapper extends
//			Mapper<Object, Text, Text, Text> {
//		// private final static IntWritable one = new IntWritable(1);
//		// private Text word = new Text();
//		//
//		// public void map(Object key,Text value,Context context) throws
//		// IOException, InterruptedException{
//		// StringTokenizer itr = new StringTokenizer(value.toString());
//		// while(itr.hasMoreTokens()){
//		// word.set(itr.nextToken());
//		// context.write(word, one);
//		// }
//		// }
//		//
//		// memeber variable
////		private Logger logger = LogManager.getLogger();
////		private Cleaner deliver_log = new Cleaner();
////
////		// clean the deliver log
////		public void map(Object key, Text value, Context context) {
////			String result = "NULL";
////			if (value != null) {
////				if (StringUtils.isNotEmpty(value.toString())) {
////					result = deliver_log.clean(value.toString());//value不为null，并且内容不为空
////				} else {
////					logger.error("输入日志Text对象value实例不为null,但是日志内容为空");
////				}
////			} else {
////				logger.error("输入日志Text对象实例value为null");
////			}
////			try {
////				context.write((Text) key, new Text(result));
////			} catch (IOException e) {
////				logger.error(e.getMessage());
////			} catch (InterruptedException e) {
////				logger.error(e.getMessage());
////			}
////
////		}
////	}
//
//	public static class LineReducer extends
//			Reducer<Text, Text, Text, Text> {
//		// private IntWritable result = new IntWritable();
//		// public void reduce(Text key,Iterable<IntWritable> values, Context
//		// context) throws IOException, InterruptedException{
//		// int sum = 0;
//		// for (IntWritable val : values) {
//		// sum += val.get();
//		// }
//		// result.set(sum);
//		// context.write(key, result);
//		// }
//		//
//		private Logger logger = LogManager.getLogger();
//		public void reduce(Text key,Text result,Context context) {
//			try {
//				context.write(new Text(""), result);
//			} catch (IOException e) {
//				logger.error(e.getMessage());
//			} catch (InterruptedException e) {
//				logger.error(e.getMessage());
//			}
//
//		}
//	}
//
//	public static void main(String[] args) throws Exception {
//		Configuration conf = new Configuration();
//		Job job = Job.getInstance(conf, "DeliverLogCleaner");
//		job.setJarByClass(MRCleaner.class);
//		job.setMapperClass(TokenizerMapper.class);
//		job.setCombinerClass(LineReducer.class);
//		job.setReducerClass(LineReducer.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(IntWritable.class);
//
//		FileInputFormat.setInputPaths(job, new Path(args[0]));
//		FileOutputFormat.setOutputPath(job, new Path(args[1]));
//		System.exit(job.waitForCompletion(true) ? 0 : 1);
//
//	}
//
//}
