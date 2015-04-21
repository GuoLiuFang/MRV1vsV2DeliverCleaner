package com.tigerjoys.clear.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tigerjoys.clear.config.Config;
import com.tigerjoys.clear.model.DeliverIn;

public class Cleaner {

	public String clean(String line) {
		// 这里返回的就是结果。判断类型，返回结果。
		String result = null;
		int log_type = getLogType(line);
		if (log_type == Config.IN_TYPE) {
			result = cleanInType(line);
		}
		if (log_type == Config.OUT_TYPE) {
			result = cleanOutType(line);
		}
		if (log_type == Config.UNKNOW_TYPE) {
			result = cleanUnknowType(line);
		}

		return result;

	}

	private String cleanUnknowType(String line) {
		return null;
	}

	private String cleanOutType(String line) {
		StringBuffer sb = new StringBuffer();
		String[] segments = splitLine(line, Config.OUT_COLON);
		String front_result = getFrontInfo(segments[Config.FRONT_SEGMENTS]);
		String back_result = getOutBackJsonInfo(segments[Config.BACK_SEGMENTS]);
		sb.append(front_result).append(Config.DELIMEITER_V_BAR).append(back_result);
		return sb.toString();
	}

	private String getOutBackJsonInfo(String back_segments) {
		JsonElement root = new JsonParser().parse(back_segments);
		StringBuffer sb = new StringBuffer();
		for (String field : Config.OUT_FIELDS) {
			
		}
		return null;
	}

	public String cleanInType(String line) {
		StringBuffer sb = new StringBuffer();
		String[] segments = splitLine(line, Config.IN_COLON);
		String front_result = getFrontInfo(segments[Config.FRONT_SEGMENTS]);
		String back_result = getInBackJsonInfo(segments[Config.BACK_SEGMENTS]);
		sb.append(front_result).append(Config.DELIMEITER_V_BAR).append(back_result);
		return sb.toString();
	}

	public String getInBackJsonInfo(String back_segments) {
		JsonElement root = new JsonParser().parse(back_segments);
		StringBuffer sb = new StringBuffer();
//		String value1 = root.getAsJsonObject().get("data").getAsJsonObject().get("field1").getAsString();
		for (String field : Config.IN_FIELDS) {
			String field_value = "NULL";
			
			if (field.equals(Config.APP_INFO_IN_1_OBJ10)) {
				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).toString();
			}else if (field.equals(Config.FILE_2_OBJ11_OBJ20)) {
				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).getAsJsonObject().get(Config.FILE_2_OBJ11_OBJ20).toString();
			}else if (field.equals(Config.PATH_IN_3_OBJ11_OBJ21)) {
				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).getAsJsonObject().get(Config.FILE_2_OBJ11_OBJ20).getAsJsonObject().get(Config.PATH_IN_3_OBJ11_OBJ21).getAsString();
			}else if (field.equals(Config.HASH_2_OBJ12)) {
				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).getAsJsonObject().get(Config.HASH_2_OBJ12).getAsString();
			}else if (field.equals(Config.EXECUTED_SOLUTIONS_IN_1_ARRAY)) {
				JsonArray ja = null;
				ja = root.getAsJsonObject().get(Config.EXECUTED_SOLUTIONS_IN_1_ARRAY).getAsJsonArray();
				field_value = "" + ja.size();
			}else if (field.equals(Config.DOWNLOAD_FILES_IN_1_ARRAY)) {
				JsonArray ja = null;
				ja = root.getAsJsonObject().get(Config.DOWNLOAD_FILES_IN_1_ARRAY).getAsJsonArray();
				field_value = "" + ja.size();
			}else {
				field_value = root.getAsJsonObject().get(field).getAsString();
			}
			
			sb.append(field_value).append(Config.DELIMEITER_V_BAR);
		}
		String result = sb.toString();
		return result.substring(0, result.length() - 1);
	}

	public String getFrontInfo(String front_segments) {
		StringBuffer sb = new StringBuffer();
		String did = "NULL";
		String record_time = "NULL";
		String pid = "NULL";
		int did_index = front_segments.indexOf(Config.DID_QUOTE);
		int end_index = front_segments.lastIndexOf(Config.BACK_QUOTE);
		did = front_segments.substring(did_index + Config.DID_QUOTE.length(), end_index);
		String record_time_pid_string = front_segments.substring(0, did_index - 1);
		String[] record_time_comma_pid = record_time_pid_string.split(Config.COMMA);
		record_time = record_time_comma_pid[Config.RECORD_TIME_INDEX];
		String millsecond_pid_string = record_time_comma_pid[Config.PID_INDEX];
		String[] millsecond_pid = millsecond_pid_string.split(Config.SPACE);
		pid = millsecond_pid[Config.PID_INDEX];
		sb.append(did).append(Config.DELIMEITER_V_BAR).append(record_time).append(Config.DELIMEITER_V_BAR).append(pid);
		return sb.toString();
	}

	private int getLogType(String line) {
		if (line.contains(Config.IN_COLON))
			return Config.IN_TYPE;
		if (line.contains(Config.OUT_COLON))
			return Config.OUT_TYPE;
		return Config.UNKNOW_TYPE;
	}
	public String[] splitLine(String line,String seperator) {
		int index = line.indexOf(seperator);
		String[] segments = new String[2];
		segments[Config.FRONT_SEGMENTS] = line.substring(0, index);
		segments[Config.BACK_SEGMENTS] = line.substring(index + seperator.length(), line.length());
		return segments;
	}

}
