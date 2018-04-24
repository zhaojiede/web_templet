package com.joy.common.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSON;

public class BaiduOcrUtils {
	private static String URL = "https://aip.baidubce.com";
	private static String API_LEY = "XrPIDT3q2vhNuUAgWDNlFvDv";
	private static String SECRET_KEY = "xDkeAp9y8OkvopPGhKzg50nlU1yGxIvK";
	private static String IMG_DIR = "/Users/shenghui555/Downloads/";

	public static void main(String[] args) {
		try {
			// getAccessToken();
			// 24.2546c110bf14389e7ae42f5f8c0355cb.2592000.1499859771.282335-9548083
			String s_img_nm = "WechatIMG390.jpeg";
//			preprocessImg("a.jpeg",s_img_nm);
			
			getGeneralImageText(IMG_DIR + s_img_nm);
			
			// getWebImageText();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void preprocessImg(String fromImg, String toImg) {
		try {
			BufferedImage img = ImageIO.read(new File(IMG_DIR + fromImg));
			// 获取图片的高宽
			int width = img.getWidth();
			int height = img.getHeight();

			// 循环执行除去干扰像素
			for (int i = 1; i < width; i++) {
				Color colorFirst = new Color(img.getRGB(i, 1));
				int numFirstGet = colorFirst.getRed() + colorFirst.getGreen() + colorFirst.getBlue();
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						Color color = new Color(img.getRGB(x, y));
						int num = color.getRed() + color.getGreen() + color.getBlue();
						if (num >= numFirstGet) {
							img.setRGB(x, y, Color.WHITE.getRGB());
						}
					}
				}
			}
			
			// 横向比较前后像素
			for (int x = 1; x < width - 1; x++) {
				for (int y = 0; y < height; y++) {
					Color color = new Color(img.getRGB(x, y));
					int num = color.getRed() + color.getGreen() + color.getBlue();
					Color p_color = new Color(img.getRGB(x-1, y));
					int p_num = p_color.getRed() + p_color.getGreen() + p_color.getBlue();
					Color n_color = new Color(img.getRGB(x+1, y));
					int n_num = n_color.getRed() + n_color.getGreen() + n_color.getBlue();
					if(num < p_num && num < n_num){
						img.setRGB(x, y, Color.WHITE.getRGB());
					}
				}
			}

//			// 图片背景变黑色
//			for (int i = 1; i < width; i++) {
//				Color color1 = new Color(img.getRGB(i, 1));
//				int num1 = color1.getRed() + color1.getGreen() + color1.getBlue();
//				for (int x = 0; x < width; x++) {
//					for (int y = 0; y < height; y++) {
//						Color color = new Color(img.getRGB(x, y));
//						int num = color.getRed() + color.getGreen() + color.getBlue();
//						if (num == num1) {
//							img.setRGB(x, y, Color.BLACK.getRGB());
//						} else {
//							img.setRGB(x, y, Color.WHITE.getRGB());
//						}
//					}
//				}
//			}
			
			// 保存图片
			File file = new File(IMG_DIR + toImg);
			if (!file.exists()) {
				File dir = file.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ImageIO.write(img, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public static String getAccessToken() throws Exception {
		String token = "";

		String uri = "/oauth/2.0/token";
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "client_credentials");
		params.put("client_id", API_LEY);
		params.put("client_secret", SECRET_KEY);

		String json = HttpUtils.doPostSSL(URL + uri, params);

//		Map<String, Object> rslt_map = JacksonUtil.convertJson2Map(json);
		@SuppressWarnings("unchecked")
		Map<String, Object> rslt_map = (Map<String, Object>) JSON.parse(json);
		token = (String) rslt_map.get("access_token");

		return token;
	}

	public static String getGeneralImageText(String imgDir) throws Exception {
		String json = "";
		String token = getAccessToken();
		String uri = "/rest/2.0/ocr/v1/general_basic?access_token=" + token;

		Map<String, String> h_params = new HashMap<String, String>();
		h_params.put("Content-Type", "application/x-www-form-urlencoded");

		Map<String, String> params = new HashMap<String, String>();
		params.put("image", getImageStr(imgDir));

		json = HttpUtils.doPostSSL(URL + uri, h_params, params);
		System.out.println(json);

		return json;
	}

	public static String getWebImageText(String imgDir) throws Exception {
		String json = "";
		String token = getAccessToken();
		String uri = "/rest/2.0/ocr/v1/webimage?access_token=" + token;

		Map<String, String> h_params = new HashMap<String, String>();
		h_params.put("Content-Type", "application/x-www-form-urlencoded");

		Map<String, String> params = new HashMap<String, String>();
		params.put("image", getImageStr(imgDir));

		json = HttpUtils.doPostSSL(URL + uri, h_params, params);
		System.out.println(json);

		return json;
	}

	public static String getImageStr(String imgDir) throws Exception {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = imgDir;// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		return Base64Utils.encodeToString(data);// 返回Base64编码过的字节数组字符串
	}

}
