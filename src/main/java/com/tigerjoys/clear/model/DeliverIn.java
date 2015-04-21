package com.tigerjoys.clear.model;

import java.io.File;
import java.util.List;


public class DeliverIn {
	private String uid;
	private String sucess;
	private String tag;
	private String pkgn;
	private String did;
	private String imei1;
	private String reqid;
	private String packageCodePath;
	private String samd;
	private String imsi1;
	private List<String> executed_solutions;
	
	private App_info app_info;
	
	private List<String> download_files;
	private String sv;
	private String host;
//	private String x-real-ip;
//	private String x-forwarded-for;
//	private String connection;
//	private String content-length;
//	private String content-type;
//	private String user-agent;
	
	public static class App_info {
		private File file;
		private String hash;
		public static class File{
			private String path;
		}
	}
	

}
