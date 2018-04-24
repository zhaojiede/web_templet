package com.joy.common.utils;

public class CaptchaUtil {
	/**
	 * 数字
	 */
	public static String DICT_NUMBER = "0123456789";
	/**
	 * 字母
	 */
	public static String DICT_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 数字和大小写字母组合
	 */
	public static String DICT_NUMCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 十六进制
	 */
	public static String HEX_NUMBER = "0123456789ABCDEF";

	/**
	 * 生成指定类型的随机字符串
	 *
	 * @param dict   字典
	 * @param length 字符串的长度 int
	 * @return String
	 */
	public static String getPassword(String dict, int length) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			sb.append(dict.charAt((int) (Math.random() * dict.length())));
		}

		return sb.toString();

	}

	public static void main(String[] args) {
		System.out.println("start");
		try {
			System.out.println("password=" + getPassword(CaptchaUtil.HEX_NUMBER, 48));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
