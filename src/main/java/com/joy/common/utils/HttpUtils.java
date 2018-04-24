package com.joy.common.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtils {
	private static Logger LOG = Logger.getLogger(HttpUtils.class);
	private static PoolingHttpClientConnectionManager HTTP_CONN_MANAGER = null;
	private static PoolingHttpClientConnectionManager HTTPS_CONN_MANAGER = null;
	private static RequestConfig HTTP_REQ_CONFIG;
	private static RequestConfig HTTPS_REQ_CONFIG;
	private static final int MAX_TIMEOUT = 7000;
	
	private static PoolingHttpClientConnectionManager getHttpConnectionManager() {
		if (HTTP_CONN_MANAGER == null) {
			try {
				initHttp();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return HTTP_CONN_MANAGER;
	}
	
	private static void initHttp() throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {
		HTTP_CONN_MANAGER = new PoolingHttpClientConnectionManager();
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		// configBuilder.setStaleConnectionCheckEnabled(true);
		HTTP_REQ_CONFIG = configBuilder.build();
	}

	private static PoolingHttpClientConnectionManager getHttpsConnectionManager() {
		if (HTTPS_CONN_MANAGER == null) {
			try {
				initHttps();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return HTTPS_CONN_MANAGER;
	}

	private static void initHttps() throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {
		SSLContext sc = createIgnoreVerifySSL();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sc)).build();
		HTTPS_CONN_MANAGER = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		// configBuilder.setStaleConnectionCheckEnabled(true);
		HTTPS_REQ_CONFIG = configBuilder.build();
	}

	/**
	 * 绕过验证
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
	
	public static String doGet(String url){
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpConnectionManager()).build();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(HTTP_REQ_CONFIG);
			
			CloseableHttpResponse response = client.execute(httpGet);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return body;
	}
	
	public static String doGetSSL(String url){
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpsConnectionManager()).build();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(HTTPS_REQ_CONFIG);
			
			CloseableHttpResponse response = client.execute(httpGet);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return body;
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPost(String url, Map<String, String> params) {
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpConnectionManager()).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(HTTP_REQ_CONFIG); 
			// 装填参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			// 设置参数到请求对象中
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			LOG.debug("请求地址：" + url);
			LOG.debug("请求参数：" + nvps.toString());

			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
//			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = client.execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return body;
	}
	
	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doJsonPost(String url, Map<String, String> headParams, String jsonStr) {
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpConnectionManager()).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(HTTP_REQ_CONFIG); 
			
			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
			if(headParams != null){
				for(Entry<String, String> entry : headParams.entrySet()){
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			
			httpPost.setHeader("Content-type", "application/json");
//			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			
			// 设置参数到请求对象中
			httpPost.setEntity(new StringEntity(jsonStr, "utf-8"));

			LOG.debug("请求地址：" + url);
			LOG.debug("请求参数：" + jsonStr);

			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = client.execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return body;
	}
	
	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPost(String url, Map<String, String> headParams, Map<String, String> params) {
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpConnectionManager()).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(HTTP_REQ_CONFIG); 
			
			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
			if(headParams != null){
				for(Entry<String, String> entry : headParams.entrySet()){
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			
//			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			
			// 装填参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			// 设置参数到请求对象中
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			LOG.debug("请求地址：" + url);
			LOG.debug("请求参数：" + nvps.toString());

			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = client.execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return body;
	}
	
	/**
	 * 发送 SSL POST 请求（HTTPS），K-V形式
	 * 
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPostSSL(String url, Map<String, String> params) {
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpsConnectionManager()).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(HTTPS_REQ_CONFIG); 
			// 装填参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			// 设置参数到请求对象中
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			LOG.debug("请求地址：" + url);
			LOG.debug("请求参数：" + nvps.toString());

			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
//			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = client.execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return body;
	}
	
	/**
	 * 发送 SSL POST 请求（HTTPS），K-V形式
	 * 
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return
	 */
	public static String doPostSSL(String url, Map<String, String> headParams, Map<String, String> params) {
		String body = "";
		try {
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(getHttpsConnectionManager()).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(HTTPS_REQ_CONFIG); 
			// 设置header信息
			if(headParams != null){
				for(Entry<String, String> entry : headParams.entrySet()){
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			
			// 装填参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			// 设置参数到请求对象中
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

			LOG.debug("请求地址：" + url);
			LOG.debug("请求参数：" + nvps.toString());

			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
//			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = client.execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			// 释放链接
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return body;
	}
}
