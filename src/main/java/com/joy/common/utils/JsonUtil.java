package com.joy.common.utils;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2013</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtil {

	public static String string2Json(String s) {
		StringBuilder sb = new StringBuilder(s.length() + 20);
		sb.append('\"');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '\"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '/':
					sb.append("\\/");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					sb.append(c);
			}
		}
		sb.append('\"');
		return sb.toString();
	}

	public static String number2Json(Number number) {
		return number.toString();
	}

	public static String boolean2Json(Boolean bool) {
		return bool.toString();
	}

	public static String date2Json(java.util.Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static String array2Json(Object[] array) {
		if (array.length == 0)
			return "[]";
		StringBuilder sb = new StringBuilder(array.length << 4);
		sb.append('[');
		for (Object o : array) {
			sb.append(toJson(o));
			sb.append(',');
		}
		// 将最后添加的 ',' 变为 ']':
		sb.setCharAt(sb.length() - 1, ']');
		return sb.toString();
	}

	public static String map2Json(Map<String, Object> map) {
		if (map.isEmpty())
			return "{}";
		StringBuilder sb = new StringBuilder(map.size() << 4);
		sb.append('{');
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object value = map.get(key);
			sb.append('\"');
			sb.append(key);
			sb.append('\"');
			sb.append(':');
			sb.append(toJson(value));
			sb.append(',');
		}
		// 将最后的 ',' 变为 '}':
		sb.setCharAt(sb.length() - 1, '}');
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static String toJson(Object o) {
		if (o == null)
			return "null";
		if (o instanceof String)
			return string2Json((String)o);
		if (o instanceof Boolean)
			return boolean2Json((Boolean)o);
		if (o instanceof Number)
			return number2Json((Number)o);
		if (o instanceof java.util.Date)
			return date2Json((java.util.Date)o);
		if (o instanceof Map)
			return map2Json((Map<String, Object>)o);
		if (o instanceof List)
			return listToJson((List<?>)o);
		if (o instanceof Object[])
			return array2Json((Object[])o);

		return beanToJson(o);
	}

	public static String beanToJson(Object obj) {
		//System.out.println("obj class=" + obj.getClass().getName());
		StringBuilder sb = new StringBuilder();
		try {

			if (obj == null) {
				return "{}";
			}
			sb.append('{');
			Field[] fields = obj.getClass().getDeclaredFields();
			ArrayList<String> fnList = new ArrayList<String>();
			ArrayList<String> fnLowList = new ArrayList<String>();
			for(int i=0; i<fields.length;i++ ){
				fnList.add(fields[i].getName());
				fnLowList.add(fields[i].getName().toLowerCase());
			}
			
			PropertyDescriptor[] voPds = Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();
			for (int i = 0; i < voPds.length; i++) {
				PropertyDescriptor voDesc = voPds[i];
				String propName = voDesc.getName();
				if(fnLowList.contains(propName.toLowerCase())){
					propName = fnList.get(fnLowList.indexOf(propName.toLowerCase()));
				}
				Method method = voDesc.getReadMethod();
				if (method == null) {
					continue;
				}
				Object object = voDesc.getReadMethod().invoke(obj);
				if (object != null && !object.getClass().getName().equals("java.lang.Class")) {
					sb.append('\"');
					sb.append(propName);
					sb.append('\"');
					sb.append(':');
					sb.append(toJson(object));
					sb.append(',');
				}

			}
			if (sb.length() > 1) {
				sb.setCharAt(sb.length() - 1, '}');
			} else {
				sb.append('}');
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}

	public static String objectToJson(Object object) {
		StringBuilder json = new StringBuilder();
		if (object == null) {
			json.append("\"\"");
		} else if (object instanceof String || object instanceof Integer) {
			json.append("\"").append(object.toString()).append("\"");
		} else if (object instanceof Map) {
			json.append(map2Json((Map<String, Object>)object));
		} else {
			json.append(beanToJson(object));
		}
		return json.toString();
	}

	public static String listToJson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static void main(String[] args) {
		

	}

}
