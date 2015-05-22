package com.tigerjoys.clear.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static Logger logger = LogManager.getLogger();
	private static Properties prop = new Properties();
	static {
		try {
			prop.load(Config.class.getClassLoader().getResourceAsStream("fields.configuration.properties"));
		} catch (IOException e) {
			logger.error("配置文件加载失败" + e.getMessage());		}
	}
	
	
	public static String LEFT_BRACE = prop.getProperty("LEFT_BRACE");
	public static String RIGHT_BRACE = prop.getProperty("RIGHT_BRACE");
	public static String QUOTE_LEFT_BRACE = prop.getProperty("QUOTE_LEFT_BRACE");
	public static String RIGHT_BRACE_QUOTE_ = prop.getProperty("RIGHT_BRACE_QUOTE_");
	public static String IN_COLON = prop.getProperty("IN_COLON");
	public static String OUT_COLON = prop.getProperty("OUT_COLON");
	public static String COMBINE_COLON = prop.getProperty("COMBINE_COLON");
	public static String IN_LOG_OUTPUT_NAME = prop.getProperty("IN_LOG_OUTPUT_NAME");
	public static String OUT_LOG_OUTPUT_NAME = prop.getProperty("OUT_LOG_OUTPUT_NAME");
	public static String COMBINE_LOG_OUTPUT_NAME = prop.getProperty("COMBINE_LOG_OUTPUT_NAME");
	public static String ERROR_LOG_OUTPUT_NAME = prop.getProperty("ERROR_LOG_OUTPUT_NAME");
	public static String DEVS = prop.getProperty("DEVS");
	public static String DEFEND = prop.getProperty("DEFEND");
	public static String COMPOSE_JSON_STRING = prop.getProperty("COMPOSE_JSON_STRING");
	
	
	public static String DID_QUOTE = prop.getProperty("DID_QUOTE");
	public static String INFO = prop.getProperty("INFO");
	public static String BACK_QUOTE = prop.getProperty("BACK_QUOTE");
	public static String SLASH_QUOTE = prop.getProperty("SLASH_QUOTE");
	public static String SLASH_N = prop.getProperty("SLASH_N");
	public static String K_NULL = prop.getProperty("K_NULL");
	public static String COMMA = prop.getProperty("COMMA");
	public static String SPACE = prop.getProperty("SPACE");
	public static String DELIMEITER_V_BAR = prop.getProperty("DELIMEITER_V_BAR");
	
	public static String APP_INFO_IN_1_OBJ10 =prop.getProperty("APP_INFO_IN_1_OBJ10");
	public static String FILE_2_OBJ11_OBJ20 =prop.getProperty("FILE_2_OBJ11_OBJ20");
	public static String PATH_IN_3_OBJ11_OBJ21 =prop.getProperty("PATH_IN_3_OBJ11_OBJ21");
	public static String HASH_2_OBJ12 =prop.getProperty("HASH_2_OBJ12");
	public static String EXECUTED_SOLUTIONS_IN_1_ARRAY =prop.getProperty("EXECUTED_SOLUTIONS_IN_1_ARRAY");
	public static String DOWNLOAD_FILES_IN_1_ARRAY =prop.getProperty("DOWNLOAD_FILES_IN_1_ARRAY");

	public static String SOLUTIONLISTCAKE_OUT_1_OBJ11 =prop.getProperty("SOLUTIONLISTCAKE_OUT_1_OBJ11");
	public static String USERCAKE_OUT_1_OBJ12 =prop.getProperty("USERCAKE_OUT_1_OBJ12");
	public static String UID_OUT_2_OBJ12 =prop.getProperty("UID_OUT_2_OBJ12");
	public static String SOLUTIONLIST_OUT_2_OBJ11_ARRAY =prop.getProperty("SOLUTIONLIST_OUT_2_OBJ11_ARRAY");
	public static String LINUX_V = prop.getProperty("LINUX_V");
	public static String SID = prop.getProperty("SID");
	public static String KNOWN_SOLUTION_ID = prop.getProperty("KNOWN_SOLUTION_ID");
	public static String VERSION = prop.getProperty("VERSION");
	public static String KNOWN_SOLUTION = prop.getProperty("KNOWN_SOLUTION");
	
	
	public static String RESPONSE =prop.getProperty("RESPONSE");
	public static String REQUEST =prop.getProperty("REQUEST");
	
	public static int IN_TYPE = Integer.parseInt(prop.getProperty("IN_TYPE"));
	public static int OUT_TYPE = Integer.parseInt(prop.getProperty("OUT_TYPE"));
	public static int COMBINE_TYPE = Integer.parseInt(prop.getProperty("COMBINE_TYPE"));
	public static int UNKNOWN_TYPE = Integer.parseInt(prop.getProperty("UNKNOWN_TYPE"));
	public static int KEY_INDEX = Integer.parseInt(prop.getProperty("KEY_INDEX"));
	public static int VALUE_INDEX = Integer.parseInt(prop.getProperty("VALUE_INDEX"));
	public static int FRONT_INDEX = Integer.parseInt(prop.getProperty("FRONT_INDEX"));
	public static int BACK_INDEX = Integer.parseInt(prop.getProperty("BACK_INDEX"));
	
	
	public static int SINGLE_SOLUTION = Integer.parseInt(prop.getProperty("SINGLE_SOLUTION"));
	
	public static int FRONT_SEGMENTS = Integer.parseInt(prop.getProperty("FRONT_SEGMENTS"));
	public static int BACK_SEGMENTS = Integer.parseInt(prop.getProperty("BACK_SEGMENTS"));
	public static int PID_INDEX = Integer.parseInt(prop.getProperty("PID_INDEX"));
	public static int RECORD_TIME_INDEX = Integer.parseInt(prop.getProperty("RECORD_TIME_INDEX"));
	
	public static String[] IN_FIELDS = getFieldsArray(prop.getProperty("IN_FIELDS"),COMMA);
	public static String[] OUT_FIELDS = getFieldsArray(prop.getProperty("OUT_FIELDS"),COMMA);
	public static String[] COMBINE_FIELDS = getFieldsArray(prop.getProperty("COMBINE_FIELDS"),COMMA);
	
	public static String[] SHELL_CODE_BOUNDARY = getFieldsArray(prop.getProperty("SHELL_CODE_BOUNDARY"),COMMA);
	public static String[] SOLUTION_ID_BOUNDARY = getFieldsArray(prop.getProperty("SOLUTION_ID_BOUNDARY"),INFO);

	private static String[] getFieldsArray(String fieldsString, String seperator) {
		return fieldsString.split(seperator);
	}
	

}
