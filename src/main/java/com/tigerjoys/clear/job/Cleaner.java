package com.tigerjoys.clear.job;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tigerjoys.clear.config.Config;

public class Cleaner {
	private Logger logger = LogManager.getLogger();
	private Gson gson = new Gson();

	public String clean(String line) {
		String result = "";
		int log_type = getLogType(line);
//		if (log_type == Config.IN_TYPE) {
//			result = cleanType(line, Config.IN_COLON);
//		}
//		if (log_type == Config.OUT_TYPE) {
//			result = cleanType(line, Config.OUT_COLON);
//		}
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
//		if (type_flag.equals(Config.IN_COLON)) {
//			front_result = getFrontInfo(segments[Config.FRONT_SEGMENTS]);
//			back_result = getInTypeBackInfo(segments[Config.BACK_SEGMENTS]);
//		}
//		if (type_flag.equals(Config.OUT_COLON)) {
//			front_result = getFrontInfo(segments[Config.FRONT_SEGMENTS]);
//			back_result = getOutTypeBackInfo(segments[Config.BACK_SEGMENTS]);
//		}
		if (type_flag.equals(Config.COMBINE_COLON)) {
			front_result = getCombineTypeFrontInfo(segments[Config.FRONT_SEGMENTS]);
			back_result = getCombineTypeBackInfo(segments[Config.BACK_SEGMENTS]);
		}
		sb.append(front_result).append(Config.DELIMEITER_V_BAR).append(back_result).append(Config.INFO).append(type_flag);
		return sb.toString();
	}

	private String getCombineTypeBackInfo(String back_segments) {
		JsonElement root = new JsonParser().parse(back_segments);
		Map<String, Object> jsonMap = gson.fromJson(back_segments,new TypeToken<Map<String, Object>>() {}.getType());
		JsonElement response_json = null;
		JsonElement request_json = null;
		if(jsonMap.containsKey(Config.RESPONSE)){
			Object tmp = jsonMap.get(Config.RESPONSE);
			if(tmp.toString().contains("{\"")){
				response_json = new JsonParser().parse(tmp.toString());
			}else{
				String response = root.getAsJsonObject().get(Config.RESPONSE).toString();
				response_json = new JsonParser().parse(response);
			}
		}	
		if (jsonMap.containsKey(Config.REQUEST)) {
			Object tmp = jsonMap.get(Config.REQUEST);
			if(tmp.toString().contains("{\"") && !tmp.toString().contains("{\"tag")){
				request_json = new JsonParser().parse(tmp.toString());
			}else{
				String request = root.getAsJsonObject().get(Config.REQUEST).toString();
				request_json = new JsonParser().parse(request);
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String field : Config.COMBINE_FIELDS) {
			String field_value = "";
			if (field.equals(Config.UID_OUT_2_OBJ12)) {
				if (objIsNull(response_json.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12)) && objIsNull(response_json.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).getAsJsonObject().get(Config.UID_OUT_2_OBJ12))) {
					field_value = response_json.getAsJsonObject().get(Config.USERCAKE_OUT_1_OBJ12).getAsJsonObject().get(Config.UID_OUT_2_OBJ12).getAsString();
				}
			} else if (field.equals(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY)) {
				String  solutionList = "";
				if (objIsNull(response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11)) && objIsNull(response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY))) {
					solutionList = response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SOLUTIONLIST_OUT_2_OBJ11_ARRAY).toString();
				}
				field_value = getSidAndSC(solutionList);
			} else if (field.equals(Config.SID)) {
				if (objIsNull(response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11)) && objIsNull(response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SID))) {
					field_value = response_json.getAsJsonObject().get(Config.SOLUTIONLISTCAKE_OUT_1_OBJ11).getAsJsonObject().get(Config.SID).getAsString();
				}
			} else if (field.equals(Config.TAG)) {
				if (objIsNull(request_json.getAsJsonObject().get(Config.TAG)) ) {
					if (request_json.getAsJsonObject().get(Config.TAG).isJsonObject()) {
						field_value = request_json.getAsJsonObject().get(Config.TAG).getAsJsonObject().get(Config.TAG).getAsString();
					}else {
						field_value = request_json.getAsJsonObject().get(Config.TAG).getAsString();
					}
				}
			} else if (field.equals(Config.VERSION)) {
				if (objIsNull(root.getAsJsonObject().get(Config.VERSION))) {
					field_value = root.getAsJsonObject().get(Config.VERSION).getAsString();
				}
			} else if (field.equals(Config.KNOWN_SOLUTION_ID)) {
				if (objIsNull(root.getAsJsonObject().get(Config.KNOWN_SOLUTION_ID))) {
					field_value = root.getAsJsonObject().get(Config.KNOWN_SOLUTION_ID).getAsString();
				}
			} else if (field.equals(Config.KNOWN_SOLUTION)) {
				if (objIsNull(root.getAsJsonObject().get(Config.KNOWN_SOLUTION))) {
					field_value = root.getAsJsonObject().get(Config.KNOWN_SOLUTION).getAsString();
				}
				
			} else if (field.equals(Config.VM_ENABLE)) {
				if (objIsNull(root.getAsJsonObject().get(Config.VM_ENABLE))) {
					field_value = root.getAsJsonObject().get(Config.VM_ENABLE).getAsString();
				}
			} else if (field.equals(Config.LINUX_V)) {
				if (objIsNull(request_json.getAsJsonObject().get(Config.LINUX_V))) {
					String linux_v_n = request_json.getAsJsonObject().get(Config.LINUX_V).getAsString();
					field_value = removeEnterSignal(linux_v_n.trim());
				}
			} else {
				if (request_json.getAsJsonObject().has(field)) {
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
//			result = result.replaceAll(Config.SLASH, "");
		}
		return result;
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

	private boolean  objIsNull(Object obj){
		if(obj!=null){
			return true;
		}
		return false;
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
//		if (line.contains(Config.IN_COLON))
//			return Config.IN_TYPE;
//		if (line.contains(Config.OUT_COLON))
//			return Config.OUT_TYPE;
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
