package com.joy.common.utils;

import java.util.HashMap;
import java.util.Map;

public class DingTalkUtils {
	private static String URL = "https://oapi.dingtalk.com";
	private static String CORPID = "dinga236da18a6335e60"; 
	private static String CORP_SECRET = "1lQPnb9UlOlBVw9CzJvabUaoeQ6vmCOfxmKPDcW_x1A_q_kJkttZaf4IOh9e4nnt"; 
	
	public static String getAccessToken() throws Exception{
		String action = "/gettoken?";
		String token = "";
		
		StringBuffer uri = new StringBuffer();
		uri.append(action);
		uri.append("corpid=");
		uri.append(CORPID);
		uri.append("&corpsecret=");
		uri.append(CORP_SECRET);
		
		token = HttpUtils.doGetSSL(URL + uri.toString());
		return token;
	}
	
	public static String sendTextMessage(String msg) throws Exception{
		String action = "/message/send_to_conversation?access_token=";
		
		String content = "{\"msgtype\": \"text\",\"text\": {\"content\": \""+msg+"\"}}";
		String token = getAccessToken();
		
		Map<String, String> params = new HashMap<String, String>();
		
		String rslt = HttpUtils.doPostSSL(action + token,params);
		return rslt;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(sendTextMessage("zhengshtest"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
