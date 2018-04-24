package com.joy.web.common.config;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ContextUtils {
	private static final Logger logger = Logger.getLogger(ContextUtils.class);
	
	public static final ArrayList<String> IMG_CONTENT_TYPE_LIST = new ArrayList<String>();
	
	//初始化map缓存数据 
	public void init() throws Exception {
		initVariable();
	}
	
	private void initVariable(){
		IMG_CONTENT_TYPE_LIST.add(".gif");
		IMG_CONTENT_TYPE_LIST.add(".jpg");
		IMG_CONTENT_TYPE_LIST.add(".png");
		IMG_CONTENT_TYPE_LIST.add(".jpeg");
	}
}
