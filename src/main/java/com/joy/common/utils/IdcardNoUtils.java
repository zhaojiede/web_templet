package com.joy.common.utils;

public class IdcardNoUtils {
	private static int[] WEIGHTS = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
	private static String[] CHECK_CODES = {"1","0","X","9","8","7","6","5","4","3","2"};
	
	public static String getLastNum(String idCardNo){
		String initStr = idCardNo;
		String rsltStr = "";
		
		if(idCardNo.length() >= 17){
			initStr = idCardNo.substring(0, 17);
		} else {
			return "-1";
		}
		
		int sumVal = 0;
		for(int i=0;i<initStr.length();i++){
			sumVal += Integer.parseInt(initStr.substring(i,i+1)) * WEIGHTS[i];
		}
		
		rsltStr = CHECK_CODES[(sumVal % 11) ];
		return rsltStr;
	}
	
	public static void main(String[] args) {
		System.out.println(getLastNum("1101"));
	}
}
