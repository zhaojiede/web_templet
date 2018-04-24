package com.joy.common.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class ZhutongSMSUTils {
	private static String url ="http://www.api.zthysms.com";
	private static String username = "hulian666hy";
	private static String password="ypA9VG";
	
	public static String sendSingleSms(String mobile, String content){
		String uri = "/sendSms.do";
		String rslt = "";
		try {
			String tkey = DateUtil.getCurDtsStr();
//			String msg= URLEncoder.encode(content, "utf-8");
			String pwd = getMD5(getMD5(password)+tkey);
			
			Map<String, String> params = new HashMap<String, String> ();
			params.put("username", username);
			params.put("password", pwd);
			params.put("tkey", tkey);
			params.put("mobile", mobile);
			params.put("content", content);
			
			rslt = HttpUtils.doPost(url+uri, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rslt;
		
		
	}
	
	public static String getRemainSmsCnt(){
		String uri = "/balance.do";
		String rslt = "";
		try {
			String tkey = DateUtil.getCurDtsStr();
//			String msg= URLEncoder.encode(content, "utf-8");
			String pwd = getMD5(getMD5(password)+tkey);
			
			Map<String, String> params = new HashMap<String, String> ();
			params.put("username", username);
			params.put("password", pwd);
			params.put("tkey", tkey);
			
			rslt = HttpUtils.doPost(url+uri, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return rslt;
	}
	
	public static String getMD5(String src) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(src.getBytes());
            byte[] s=m.digest();

            return bintoascii(s);
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public static String bintoascii(byte[] bySourceByte) {
        int len, i;
        byte tb;
        char high, tmp, low;
        String result = new String();
        len = bySourceByte.length;
        for (i = 0; i < len; i++) {
            tb = bySourceByte[i];

            tmp = (char) ((tb >>> 4) & 0x000f);
            if (tmp >= 10) {
                high = (char) ('a' + tmp - 10);
            } else {
                high = (char) ('0' + tmp);
            }
            result += high;
            tmp = (char) (tb & 0x000f);
            if (tmp >= 10) {
                low = (char) ('a' + tmp - 10);
            } else {
                low = (char) ('0' + tmp);
            }

            result += low;
        }
        return result;
    }

	public static void main(String[] args) {
//		sendSingleSms("18600047117","【犀客空间】犀客空间您的开门密码是122222，有效期自2017-05-20 09:58:18至2017-05-20 11:58:18止，请您妥善保存。地址：北京市国贸CBD光华路甲8号-8号和乔大厦3001");
		System.out.println(getRemainSmsCnt());
	}

}
