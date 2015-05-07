package com.tigerjoys.clear.job;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tigerjoys.clear.config.Config;

public class Cleaner {
	private Logger logger = LogManager.getLogger();

	public String clean(String line) {
		// 这里返回的就是结果。判断类型，返回结果。
		String result = "";
		int log_type = getLogType(line);
		if (log_type == Config.IN_TYPE) {
			result = cleanType(line, Config.IN_COLON);
		}
		if (log_type == Config.OUT_TYPE) {
			result = cleanType(line, Config.OUT_COLON);
		}
		if (log_type == Config.COMBINE_TYPE) {
			result = cleanType(line, Config.COMBINE_COLON);
		}
		if (log_type == Config.UNKNOWN_TYPE) {
			result = "NULL" + Config.INFO + "NULL";
			logger.error("UNKNOWN_TYPE,日志格式不对" + line);
		}
		return result;
	}

	private String cleanType(String line, String type_flag) {
		StringBuffer sb = new StringBuffer();
		String[] segments = splitLine(line, type_flag);
		String front_result = "";
		String back_result = "";
		if (type_flag.equals(Config.IN_COLON)) {
			front_result = getFrontInfo(segments[Config.FRONT_SEGMENTS]);
			back_result = getInTypeBackInfo(segments[Config.BACK_SEGMENTS]);
		}
		if (type_flag.equals(Config.OUT_COLON)) {
			front_result = getFrontInfo(segments[Config.FRONT_SEGMENTS]);
			back_result = getOutTypeBackInfo(segments[Config.BACK_SEGMENTS]);
		}
		if (type_flag.equals(Config.COMBINE_COLON)) {
			front_result = getCombineTypeFrontInfo(segments[Config.FRONT_SEGMENTS]);
			back_result = getCombineTypeBackInfo(segments[Config.BACK_SEGMENTS]);
		}
		sb.append(front_result).append(Config.DELIMEITER_V_BAR).append(back_result).append(Config.INFO).append(type_flag);
		return sb.toString();
	}



	private String getCombineTypeBackInfo(String back_segments) {
		String request_response = "";
		int back_index = back_segments.indexOf(Config.DEVS);
		if (back_index < 0) {
			request_response = back_segments;
		}else{
			request_response = back_segments.substring(0, back_index);
			request_response += Config.COMPOSE_JSON_STRING;
		}
		JsonElement root = new JsonParser().parse(request_response);
		String response = root.getAsJsonObject().get(Config.RESPONSE).getAsString();
//		response = formateJsonString(response);
		JsonElement response_json = new JsonParser().parse(response);
		//------------//
		String request = root.getAsJsonObject().get(Config.REQUEST).getAsString();
//		request = formateJsonString(request);
		JsonElement request_json = new JsonParser().parse(request);
		
		StringBuffer sb = new StringBuffer();
		for (String field : Config.COMBINE_FIELDS) {
			String field_value = "";
			if (field.equals(Config.UID_OUT_2_OBJ12)) {
				field_value = "";
				if (objIsNull(response_json.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12)) && objIsNull(response_json.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).getAsJsonObject().get(Config.UID_OUT_2_OBJ12))) {
					field_value = response_json.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).getAsJsonObject().get(Config.UID_OUT_2_OBJ12).getAsString();
				}
			} else if (field.equals(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY)) {
				String  solutionList = "";
				if (objIsNull(response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11)) && objIsNull(response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY))) {
					solutionList = response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY).toString();
				}
				field_value = getSidAndSC(solutionList);
			} else if (field.equals(Config.LINUX_V)) {
				if (objIsNull(request_json.getAsJsonObject().get(Config.LINUX_V))) {
					String linux_v_n = request_json.getAsJsonObject().get(Config.LINUX_V).getAsString();
					field_value = removeEnterSignal(linux_v_n.trim());
				}
			} else {
				if (objIsNull(request_json.getAsJsonObject().get(field))) {
					field_value = request_json.getAsJsonObject().get(field).getAsString();
				}
			}
			sb.append(field_value).append(Config.DELIMEITER_V_BAR);
		}//for--loop
		return sb.substring(0,sb.length()-1);
	}

	private String removeEnterSignal(String source) {
		String result = "";
		if (StringUtils.isNotEmpty(source)) {
			result = source.replaceAll(Config.SLASH_N, "");
		}
		return result;
	}

	private String formateJsonString(String source) {
		source.replaceAll(Config.SLASH_QUOTE, Config.BACK_QUOTE);
		return source;
	}

	private String getOutTypeBackInfo(String back_segments) {
		JsonElement root = new JsonParser().parse(back_segments);
		StringBuffer sb = new StringBuffer();
		for (String field : Config.OUT_FIELDS) {
			String field_value = "";
//			if (field.equals(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11)) {
//				field_value = root.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).toString();
//			} else if (field.equals(Config.USERCAKE_OUT_1_OBJ12)) {
//				field_value = root.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).toString();
//			} else 
			if (field.equals(Config.UID_OUT_2_OBJ12)) {
				field_value = "";
				if(objIsNull(root.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12)) && objIsNull(root.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).getAsJsonObject().get(Config.UID_OUT_2_OBJ12))){
					field_value = root.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).getAsJsonObject().get(Config.UID_OUT_2_OBJ12).getAsString();
				}
			} else if (field.equals(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY)) {
//				JsonArray ja = new JsonArray();
//				ja = root.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY).getAsJsonArray();
//				field_value = "" + ja.size();
				String  solutionList = "";
				if (objIsNull(root.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11)) && objIsNull(root.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY))) {
					solutionList = root.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY).toString();
				}
				field_value = getSidAndSC(solutionList);
			} else {
				if (objIsNull(root.getAsJsonObject().get(field))) {
					field_value = root.getAsJsonObject().get(field).getAsString();
				}
			}
			sb.append(field_value).append(Config.DELIMEITER_V_BAR);
		}//for--loop
		String result = sb.toString();
		return result.substring(0, result.length() - 1);
	}

	private String getSidAndSC(String solutionList) {
		StringBuffer sb = new StringBuffer();
		String shell_code = "";
		String solution_id = "";
		if (!solutionList.equals("")) {
			JsonElement root = new JsonParser().parse(solutionList);
			JsonArray ja = new JsonArray();
			int size = 0;
			ja = root.getAsJsonArray();
			size = ja.size();
			if (size == Config.SINGLE_SOLUTION) {
				shell_code = indexOfString(solutionList,Config.SHELL_CODE_BOUNDARY);
				solution_id = indexOfString(solutionList, Config.SOLUTION_ID_BOUNDARY);
			} 
		}
		sb.append(solutionList).append(Config.DELIMEITER_V_BAR).append(solution_id).append(Config.DELIMEITER_V_BAR).append(shell_code);
		
		return sb.toString();
	}

	private String indexOfString(String solutionList,String[] boundary) {
		String result = "";
		int front_boundary = solutionList.indexOf(boundary[Config.FRONT_INDEX]);
		if (front_boundary < 0 ) {
			return result;
		}
		int back_boundary = solutionList.indexOf(boundary[Config.BACK_INDEX], front_boundary + boundary[Config.FRONT_INDEX].length());
		result = solutionList.substring(front_boundary + boundary[Config.FRONT_INDEX].length(), back_boundary);
		return result;
	}

	private String getInTypeBackInfo(String back_segments) {
		JsonElement root = new JsonParser().parse(back_segments);
		StringBuffer sb = new StringBuffer();
		// String value1 =
		// root.getAsJsonObject().get("data").getAsJsonObject().get("field1").getAsString();
		for (String field : Config.IN_FIELDS) {
			String field_value = "";

			if (field.equals(Config.APP_INFO_IN_1_OBJ10)) {
				if(objIsNull(root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10))){
					field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).toString();
				}
				
//			} else if (field.equals(Config.FILE_2_OBJ11_OBJ20)) {
//				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).getAsJsonObject().get(Config.FILE_2_OBJ11_OBJ20).toString();
//			} else if (field.equals(Config.PATH_IN_3_OBJ11_OBJ21)) {
//				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).getAsJsonObject().get(Config.FILE_2_OBJ11_OBJ20).getAsJsonObject().get(Config.PATH_IN_3_OBJ11_OBJ21).getAsString();
//			} else if (field.equals(Config.HASH_2_OBJ12)) {
//				field_value = root.getAsJsonObject().get(Config.APP_INFO_IN_1_OBJ10).getAsJsonObject().get(Config.HASH_2_OBJ12).getAsString();
			} else if (field.equals(Config.EXECUTED_SOLUTIONS_IN_1_ARRAY)) {
//				JsonArray ja = new JsonArray();
//				ja = root.getAsJsonObject().get(Config.EXECUTED_SOLUTIONS_IN_1_ARRAY).getAsJsonArray();
//				field_value = "" + ja.size();
				if (objIsNull(root.getAsJsonObject().get(Config.EXECUTED_SOLUTIONS_IN_1_ARRAY))) {
					field_value = root.getAsJsonObject().get(Config.EXECUTED_SOLUTIONS_IN_1_ARRAY).toString();
				}
			} else if (field.equals(Config.DOWNLOAD_FILES_IN_1_ARRAY)) {
//				JsonArray ja = new JsonArray();
//				ja = root.getAsJsonObject().get(Config.DOWNLOAD_FILES_IN_1_ARRAY).getAsJsonArray();
//				field_value = "" + ja.size();
				if (objIsNull(root.getAsJsonObject().get(Config.DOWNLOAD_FILES_IN_1_ARRAY))) {
					field_value = root.getAsJsonObject().get(Config.DOWNLOAD_FILES_IN_1_ARRAY).toString();
				}
			} else {
				if (objIsNull(root.getAsJsonObject().get(field))) {
					field_value = root.getAsJsonObject().get(field).getAsString();
				}
			}
			sb.append(field_value).append(Config.DELIMEITER_V_BAR);
		}//for--loop
		String result = sb.toString();
		return result.substring(0, result.length() - 1);
	}
	
	
	private boolean  objIsNull(Object obj){
		if(obj!=null){
			return true;
		}
		return false;
	}

	private String getFrontInfo(String front_segments) {
		//考虑日志的不完整性，这是这次思路的问题，以为有了日志，就会是正确的日志，这个思路是不对的。
		//往下分析日志有一个前提是，假设日志是不正确的情况。
		StringBuffer sb = new StringBuffer();
		String did = "";
		String record_time = "";
		String pid = "";
		int did_index = front_segments.indexOf(Config.DID_QUOTE);
		int end_index = front_segments.lastIndexOf(Config.BACK_QUOTE);
		if(did_index > -1 && end_index > did_index + Config.DID_QUOTE.length()){
			did = front_segments.substring(did_index + Config.DID_QUOTE.length(), end_index);
		}else {
			did_index = front_segments.length();
		}
		String	record_time_pid_string = front_segments.substring(0, did_index).trim();
		String[] record_time_comma_pid = record_time_pid_string.split(Config.COMMA);
		record_time = record_time_comma_pid[Config.RECORD_TIME_INDEX];
		String millsecond_pid_string = record_time_comma_pid[Config.PID_INDEX];
		String[] millsecond_pid = millsecond_pid_string.split(Config.SPACE);
		pid = millsecond_pid[Config.PID_INDEX];
		sb.append(did).append(Config.DELIMEITER_V_BAR).append(record_time).append(Config.DELIMEITER_V_BAR).append(pid);
		return sb.toString();
	}
	private String getCombineTypeFrontInfo(String front_segments) {
		StringBuffer sb = new StringBuffer();
		String record_time = "";
		String pid = "";
		int back_index = front_segments.indexOf(Config.INFO);
		if (back_index < 0) {
			back_index = front_segments.length();
		}
		String record_time_pid_string = front_segments.substring(0, back_index).trim();
		String[] record_time_comma_pid = record_time_pid_string.split(Config.COMMA);
		record_time = record_time_comma_pid[Config.RECORD_TIME_INDEX];
		String millsecond_pid_string = record_time_comma_pid[Config.PID_INDEX];
		String[] millsecond_pid = millsecond_pid_string.split(Config.SPACE);
		pid = millsecond_pid[Config.PID_INDEX];
		sb.append(record_time).append(Config.DELIMEITER_V_BAR).append(pid);
		return sb.toString();
	}

	private int getLogType(String line) {
		if (line.contains(Config.IN_COLON))
			return Config.IN_TYPE;
		if (line.contains(Config.OUT_COLON))
			return Config.OUT_TYPE;
		if (line.contains(Config.COMBINE_COLON))
			return Config.COMBINE_TYPE;
		return Config.UNKNOWN_TYPE;
	}

	private String[] splitLine(String line, String seperator) {
		int index = line.indexOf(seperator);
		String[] segments = new String[2];
		segments[Config.FRONT_SEGMENTS] = line.substring(0, index);
		segments[Config.BACK_SEGMENTS] = line.substring(index + seperator.length(), line.length());
		return segments;
	}

}
