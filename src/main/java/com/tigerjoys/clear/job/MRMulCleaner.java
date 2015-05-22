package com.tigerjoys.clear.job;

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

	public static class LogTokenizerMapper extends Mapper<Object, Text, Text, Text> {
		private Logger logger = LogManager.getLogger();
		private Cleaner deliver_log = new Cleaner();

		public void map(Object key, Text value, Context context) {
			String result = "";
			if (value != null) {
				if (StringUtils.isNotEmpty(value.toString().trim())) {
					result = deliver_log.clean(value.toString().trim());// value不为null，并且内容不为空
				} else {
					result = "NULL" + Config.INFO + "NULL";
					logger.error("输入日志Text对象value实例不为null,但是日志内容为空");
				}
			} else {
				result = "NULL" + Config.INFO + "NULL";
				logger.error("输入日志Text对象实例value为null");
			}
			try {
				// 对result进行分解。
				String[] key_value = result.split(Config.INFO);// 把错误信息直接输出到mapReduce中去。
				context.write(new Text(key_value[Config.KEY_INDEX]), new Text(key_value[Config.VALUE_INDEX]));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}// map()--method

	}// LogTokenizerMapper--class

	public static class LogReducer extends Reducer<Text, Text, Text, Text> {
		private Logger logger = LogManager.getLogger();
		private Cleaner deliver_log = new Cleaner();
		private MultipleOutputs mos;

		public void setup(Context context) {
			mos = new MultipleOutputs(context);
		}// setup--method

		public void reduce(Text key, Iterable<Text> values, Context context) {
			// if (key.toString().equals(Config.IN_COLON)) {
			// for (Text value : values) {
			// try {
			// mos.write(Config.IN_LOG_OUTPUT_NAME, NullWritable.get(), value);
			// } catch (Exception e) {
			// logger.error(e.getMessage());
			// }
			// }
			// }
			// if (key.toString().equals(Config.OUT_COLON)) {
			// for (Text value : values) {
			// try {
			// mos.write(Config.OUT_LOG_OUTPUT_NAME, NullWritable.get(), value);
			// } catch (IOException e) {
			// logger.error(e.getMessage());
			// } catch (InterruptedException e) {
			// logger.error(e.getMessage());
			// }
			// }
			// }
			if (key.toString().equals(Config.COMBINE_COLON)) {
				for (Text value : values) {
					try {
						mos.write(Config.COMBINE_LOG_OUTPUT_NAME,
								NullWritable.get(), value);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			if (key.toString().equals("NULL")) {
				for (Text value : values) {
					try {
						mos.write(Config.ERROR_LOG_OUTPUT_NAME,
								NullWritable.get(), value);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}

		}// reduce--method

		public void cleanup(Context context) {
			try {
				mos.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}// cleanup--method
	}// LogReducer--class

	public static void main(String[] args) {
		// /data/sword/input/processed_1000.txt
		// args = new String[2];
		// args[0] = "hdfs://localhost:19000/data/sword/input/";
		// args[1] = "hdfs://localhost:19000/data/sword/output/";
		Configuration conf = new Configuration();
		Job job = null;
		try {
			job = Job.getInstance(conf, "多输出清洗");
		} catch (Exception e) {
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
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		// MultipleOutputs.addNamedOutput(job, Config.IN_LOG_OUTPUT_NAME,
		// TextOutputFormat.class, NullWritable.class, Text.class);
		// MultipleOutputs.addNamedOutput(job, Config.OUT_LOG_OUTPUT_NAME,
		// TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, Config.COMBINE_LOG_OUTPUT_NAME,	TextOutputFormat.class, NullWritable.class, Text.class);
		MultipleOutputs.addNamedOutput(job, Config.ERROR_LOG_OUTPUT_NAME, TextOutputFormat.class, NullWritable.class, Text.class);
		try {
			System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}// main--method

}