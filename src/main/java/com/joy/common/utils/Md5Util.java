package com.joy.common.utils;

import java.security.MessageDigest;

public class Md5Util {
	public Md5Util() {
	}

	public static String byte2hex(byte[] b) { // 二行制转字符
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static String getMD5(String password) {
		String returnStr = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");

			alga.update(password.getBytes());
			byte[] digesta = alga.digest();
			returnStr = byte2hex(digesta);
		} catch (Exception e) {
		}
		return returnStr;
	}

	public static String encodeMD5(String inStr) {
		
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	public static String decodeMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}
	
	public static String encode(String str) {
		return encodeMD5(getMD5(str));
	}
	
	public static String decode(String str) {
		return decodeMD5(str);
	}
	
	public static String getMD5Lower(String password) {
		String returnStr = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");

			alga.update(password.getBytes());
			byte[] digesta = alga.digest();
			returnStr = byte2hex(digesta);
		} catch (Exception e) {
		}
		return returnStr.toLowerCase();
	}
	
	public static void main(String args[]) {
		
		long nd = 1000 * 2 * 60 * 60;
		
		long cur =System.currentTimeMillis();
		long end = cur + nd;//俩小时
		
//
		Md5Util m = new Md5Util();
//		
//		System.out.println("MD5 Test suite:");
//		System.out.println("MD5原始:admin");
		
//		System.out.println("sl744534后:" +m.getMD5("sl744534"));
//		System.out.println("113389后:" +m.getMD5("113389"));
//		System.out.println("zhangyu006后:" +m.getMD5("zhangyu006"));
//		System.out.println("lihong005后:" +m.getMD5("lihong005"));
//		System.out.println("zhuwei003后:" +m.getMD5("zhuwei003"));
//		System.out.println("jingjing588后:" +m.getMD5("jingjing588"));
//		System.out.println("wangchun009后:" +m.getMD5("wangchun009"));
//		System.out.println("zhenning233后:" +m.getMD5("zhenning233"));
//		System.out.println("baoning100后:" +m.getMD5("baoning100"));
//		System.out.println("lijia009后:" +m.getMD5("lijia009"));
//		System.out.println("suofei366后:" +m.getMD5("suofei366"));
		
		System.out.println(m.getMD5("1"));
		
		System.out.println( m.decode("CF8B1A5C92F36BDB3F30DD3FE5930782"));
//		System.out.println("MD5后在加密:" + encode("admin"));
//		System.out.println("MD5后在解密:" + decode("FEFGF2FMC5AC5A5C@GLM@5D1@5LDE27G"));
//
	}

}

