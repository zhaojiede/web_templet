package com.joy.common.utils;
///*
// * @(#)JacksonUtil.java $version 2011-09-05
// *
// * Copyright 2011 NHN Corp. All rights Reserved. 
// * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
// */
//
//package com.hlj.common.utils;
//
//import java.beans.BeanInfo;
//import java.beans.IntrospectionException;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.codehaus.jackson.JsonFactory;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.DeserializationConfig;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.SerializationConfig;
//import org.codehaus.jackson.type.TypeReference;
//
///**
// * @author CN13358
// * @date 2011-09-05
// * @param <T>
// */
//@SuppressWarnings("deprecation")
//public class JacksonUtil<T> {
//	private static Logger log = Logger.getLogger(JacksonUtil.class);
//	private static final ObjectMapper MAPPER = new ObjectMapper();
//	static {
//		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		MAPPER.getDeserializationConfig().setDateFormat(df);
//		MAPPER.getSerializationConfig().setDateFormat(df);
//		MAPPER.configure(SerializationConfig.Feature.USE_STATIC_TYPING, true);
//		MAPPER.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	}
//
//	public static <T> T convertJson2Bean(String json, Class<T> clazz) {
//		if (StringUtils.isBlank(json)) {
//			return null;
//		}
//		try {
//			return (T)MAPPER.readValue(json, clazz);
//		} catch (IOException e) {
//			log.error("parse json Error", e);
//		}
//		return null;
//	}
//
//	public static Map<String, Object> convertJson2Map(String json) {
//		JsonFactory factory = new JsonFactory();
//		ObjectMapper mapper = new ObjectMapper(factory);
//		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
//		};
//		try {
//			return mapper.readValue(json, typeRef);
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static List<Map<String, Object>> convertJson2List(String json) {
//		JsonFactory factory = new JsonFactory();
//		ObjectMapper mapper = new ObjectMapper(factory);
//		TypeReference<List<HashMap<String, Object>>> typeRef = new TypeReference<List<HashMap<String, Object>>>() {
//		};
//		try {
//			return mapper.readValue(json, typeRef);
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 将一个 Map 对象转化为一个 JavaBean
//	 * 
//	 * @param type
//	 *            要转化的类型
//	 * @param map
//	 *            包含属性值的 map
//	 * @return 转化出来的 JavaBean 对象
//	 * @throws IntrospectionException
//	 *             如果分析类属性失败
//	 * @throws IllegalAccessException
//	 *             如果实例化 JavaBean 失败
//	 * @throws InstantiationException
//	 *             如果实例化 JavaBean 失败
//	 * @throws InvocationTargetException
//	 *             如果调用属性的 setter 方法失败
//	 */
//	@SuppressWarnings("rawtypes")
//	public static Object convertMap2Bean(Class type, Map map) throws IntrospectionException,
//		IllegalAccessException,
//		InstantiationException,
//		InvocationTargetException {
//		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
//		Object obj = type.newInstance(); // 创建 JavaBean 对象
//
//		// 给 JavaBean 对象的属性赋值
//		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//		for (int i = 0; i < propertyDescriptors.length; i++) {
//			PropertyDescriptor descriptor = propertyDescriptors[i];
//			String propertyName = descriptor.getName();
//
//			if (map.containsKey(propertyName)) {
//				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
//				Object value = map.get(propertyName);
//
//				Object[] args = new Object[1];
//				args[0] = value;
//
//				descriptor.getWriteMethod().invoke(obj, args);
//			}
//		}
//		return obj;
//	}
//
//	public String doJson(T dataObject) throws IOException {
//		return MAPPER.writeValueAsString(dataObject);
//	}
//
//	public static String doJackson(Object value) {
//		String rtnJSON = null;
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			rtnJSON = mapper.writeValueAsString(value);
//		} catch (JsonGenerationException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return rtnJSON;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static void main(String[] args) {
//		String tmp="";
////		tmp = "[{\"fileDesc\":\"个人信用报告\"},{\"fileDesc\":\"个人信用报告需开具15日之内的\"},{\"fileDesc\":\"<a target=\\\"blank\\\" href=\\\"http://www.pbccrc.org.cn/zxzx/lxfs/lxfs.shtml\\\" class=\\\"tx_stl11\\\">全国各地征信中心联系方式查询&gt;&gt;</a>\"}]";
//		tmp="{\"ReqID\":\"143e43f9d2\",\"ErrNo\":0,\"ErrMsg\":\"\",\"access_token\":\"310b7ec7d940685f02ae256d0d33704714b173876386fbb2816c5e0de0d95c3b71159d36a23de54f5fbcf75d0ff8ba886cab84bfa1383a85a12042c2afb64a4f\",\"expires_time\":1487387459}";
//		tmp = "{\"expires_time\":1487387459}";
////		List list = convertJson2List(tmp);
////		System.out.println(list);
//		try {
//			String a = doJackson(tmp);
//			System.out.println(a);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		
//	}
//}
