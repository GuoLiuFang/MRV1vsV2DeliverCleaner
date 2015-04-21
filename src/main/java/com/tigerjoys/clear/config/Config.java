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
	public static String IN_COLON = prop.getProperty("IN_COLON");
	public static String OUT_COLON = prop.getProperty("OUT_COLON");
	public static String DID_QUOTE = prop.getProperty("DID_QUOTE");
	public static String BACK_QUOTE = prop.getProperty("BACK_QUOTE");
	public static String COMMA = prop.getProperty("COMMA");
	public static String SPACE = prop.getProperty("SPACE");
	public static String DELIMEITER_V_BAR = prop.getProperty("DELIMEITER_V_BAR");
	
	public static String APP_INFO_IN_1_OBJ10 =prop.getProperty("APP_INFO_IN_1_OBJ10");
	public static String FILE_2_OBJ11_OBJ20 =prop.getProperty("FILE_2_OBJ11_OBJ20");
	public static String PATH_IN_3_OBJ11_OBJ21 =prop.getProperty("PATH_IN_3_OBJ11_OBJ21");
	public static String HASH_2_OBJ12 =prop.getProperty("HASH_2_OBJ12");
	public static String EXECUTED_SOLUTIONS_IN_1_ARRAY =prop.getProperty("EXECUTED_SOLUTIONS_IN_1_ARRAY");
	public static String DOWNLOAD_FILES_IN_1_ARRAY =prop.getProperty("DOWNLOAD_FILES_IN_1_ARRAY");
	
	public static int IN_TYPE = Integer.parseInt(prop.getProperty("IN_TYPE"));
	public static int OUT_TYPE = Integer.parseInt(prop.getProperty("OUT_TYPE"));
	public static int UNKNOW_TYPE = Integer.parseInt(prop.getProperty("UNKNOW_TYPE"));
	
	public static int FRONT_SEGMENTS = Integer.parseInt(prop.getProperty("FRONT_SEGMENTS"));
	public static int BACK_SEGMENTS = Integer.parseInt(prop.getProperty("BACK_SEGMENTS"));
	public static int PID_INDEX = Integer.parseInt(prop.getProperty("PID_INDEX"));
	public static int RECORD_TIME_INDEX = Integer.parseInt(prop.getProperty("RECORD_TIME_INDEX"));
	
	public static String[] IN_FIELDS = getFieldsArray(prop.getProperty("IN_FIELDS"),COMMA);
	public static String[] OUT_FIELDS = getFieldsArray(prop.getProperty("OUT_FIELDS"),COMMA);

	private static String[] getFieldsArray(String fieldsString, String seperator) {
		return fieldsString.split(seperator);
	}
	

}
