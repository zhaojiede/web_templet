package com.joy.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSDK;

public class YtxSmsUtils {

	private static CCPRestSDK restAPI = new CCPRestSDK();
	private static String smsTemplateId = "173594"; //短信验证码模板
	private static String smsRoomPasswordId = "137908"; //预定成功发送门锁密码模板
	private static String smsWifiInfoId = "165293"; //发送房间wifi信息
	private static String smsCommitOrderNoticeRelatedEmpTId = "175531"; //支付成功通知给员工推送
	private static String smsCancelOrderNoticeRelatedEmpTId = "175533"; //撤销顶顶那通知给员工推送
	private static String smsCheckOutOrderNoticeRelatedEmpTId = "175526"; //退房通知给员工推送
	private static String smsAdvanceNoticRelatedEmpTId = "175527"; //入住当天，提前通知，给房源的相关人员
	private static String smsRoomPasswordAndWifiForOrderPayTId = "175784"; //支付订单时 发送门锁密码和wifi信息 给用户
	private static String smsRoomOrderInfoForAddOrderTId = "176098"; //下单时，给用户发送预定简单通知
	
	
	public void init() throws Exception {
	//restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口 (开发) Rest URL：
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口 (生产) Rest URL：
		restAPI.setAccount("8a48b5514fba2f87014fcadef0a0248c", "8aadb30a9d594208926cf30a90017cc6");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("8a48b5514fba2f87014fcae451a0249c");// 初始化应用ID
	}

	public static HashMap<String, Object> sendTemplateSms(ArrayList<String> mobileList, String smsTemplateId,
		String[] params) {
		HashMap<String, Object> rsltMap = restAPI.sendTemplateSMS(mobileList.toString().replaceAll("[\\]\\[\\s]", ""),
			smsTemplateId, params);
		return rsltMap;
	}

	public static HashMap<String, Object> sendVoiceVerify(String code, String mobile, String dispPhoneNo, String playCnt) {
		HashMap<String, Object> rsltMap = restAPI.voiceVerify(code, mobile, dispPhoneNo, playCnt, "");
		return rsltMap;
	}

	/**
	 * 
	 * @param mobileNo
	 * @param mobileCode
	 * @return boolean
	 */
	public static boolean doSend(String mobileNo, String mobileCode, String sendType) {
		HashMap<String, Object> result = null;
		if ("sms".equals(sendType)) {
			ArrayList<String> mobileList = new ArrayList<String>();
			mobileList.add(mobileNo);
			result = YtxSmsUtils.sendTemplateSms(mobileList, smsTemplateId, new String[] {mobileCode});
		} else {
			result = YtxSmsUtils.sendVoiceVerify(mobileCode, mobileNo, "4000044411", "3");
		}
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param mobileNo
	 * @param roomAddr
	 * @param pwdStartDt
	 * @param pwdEndDts
	 * @param doorPwd
	 * @return boolean
	 */
	public static boolean sendRoomPasswordSms(String mobileNo,String roomAddr,String pwdStartDts,String pwdEndDts,String doorPwd) {
		HashMap<String, Object> result = null;
		ArrayList<String> mobileList = new ArrayList<String>();
		mobileList.add(mobileNo);
		result = YtxSmsUtils.sendTemplateSms(mobileList, smsRoomPasswordId, new String[] {roomAddr,pwdStartDts,pwdEndDts,doorPwd});
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param userMobile
	 * @param roomAddr
	 * @param wifiNm
	 * @param wifiPwd
	 * @return
	 */
	public static boolean sendRoomWifiInfoSms(String mobileNo, String roomAddr, String wifiNm, String wifiPwd) {
		HashMap<String, Object> result = null;
		ArrayList<String> mobileList = new ArrayList<String>();
		mobileList.add(mobileNo);
		result = YtxSmsUtils.sendTemplateSms(mobileList, smsWifiInfoId, new String[] {roomAddr,wifiNm,wifiPwd});
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * 支付成功发送给员工的推送短信
	 * @param mobileList
	 * @param smsParams
	 * @return
	 */
	public static boolean sendCommitOrderNoticeRelatedEmp(List<String> mobileList, String[] smsParams) {
		HashMap<String, Object> result = null;
		result = YtxSmsUtils.sendTemplateSms((ArrayList<String>)mobileList, smsCommitOrderNoticeRelatedEmpTId, smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 撤销订单发送给员工的推送短信
	 * @param mobileList
	 * @param smsParams
	 * @return
	 */
	public static boolean sendCancelOrderNoticeRelatedPersonnel(List<String> mobileList, String[] smsParams) {
		HashMap<String, Object> result = null;
		result = YtxSmsUtils.sendTemplateSms((ArrayList<String>)mobileList, smsCancelOrderNoticeRelatedEmpTId, smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 退房发送给员工的推送短信
	 * @param mobileList
	 * @param smsParams
	 * @return
	 */
	public static boolean sendCheckOutOrderNoticeRelatedPersonnel(List<String> mobileList, String[] smsParams) {
		HashMap<String, Object> result = null;
		result = YtxSmsUtils.sendTemplateSms((ArrayList<String>)mobileList, smsCheckOutOrderNoticeRelatedEmpTId, smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 入住当天，提前通知，给房源的相关人员
	 * @param mobileList
	 * @param smsParams
	 * @return
	 */
	public static boolean sendAdvanceNoticRelatedPersonnel(List<String> mobileList, String[] smsParams) {
		HashMap<String, Object> result = null;
		result = YtxSmsUtils.sendTemplateSms((ArrayList<String>)mobileList, smsAdvanceNoticRelatedEmpTId, smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 支付订单时 发送门锁密码和wifi信息 给用户
	 * @param userMobile
	 * @param smsParams
	 * @return
	 */
	public static boolean sendRoomPasswordAndWifiForOrderPay(String mobileNo, String[] smsParams) {
		HashMap<String, Object> result = null;
		ArrayList<String> mobileList = new ArrayList<String>();
		mobileList.add(mobileNo);
		result = YtxSmsUtils.sendTemplateSms(mobileList, smsRoomPasswordAndWifiForOrderPayTId,smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean sendRoomOrderInfoForAddOrder(String mobileNo, String[] smsParams) {
		HashMap<String, Object> result = null;
		ArrayList<String> mobileList = new ArrayList<String>();
		mobileList.add(mobileNo);
		result = YtxSmsUtils.sendTemplateSms(mobileList, smsRoomOrderInfoForAddOrderTId,smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean sendAddRoomOrderNoticeRelatedPersonnel(List<String> mobileList, String[] smsParams) {
		HashMap<String, Object> result = null;
		result = YtxSmsUtils.sendTemplateSms((ArrayList<String>) mobileList, smsRoomOrderInfoForAddOrderTId,smsParams);
		if ("000000".equals(result.get("statusCode"))) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		HashMap<String, Object> result = null;
		YtxSmsUtils ytxSmsUtils = new YtxSmsUtils();
		try {
			ytxSmsUtils.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<String> mobileList = new ArrayList<String>();
		mobileList.add("13261450279");

		System.out.println(mobileList.toString().replaceAll("[\\]\\[\\s]", ""));

		result = sendTemplateSms(mobileList,"155339" ,new String[]{"123456"});
//		result = sendVoiceVerify("123459", "18600405290", "4001516516", "3");

		if ("000000".equals(result.get("statusCode"))) {
			//正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>)result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				System.out.println(key + " = " + object);
			}
		} else {
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
		}
	}


}
