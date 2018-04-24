package com.joy.common.utils;

/**
 * <p>Title: miduo360</p>
 * <p>Description: DateUtil </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: Shanghai Mydream Information Technology Co.,Ltd</p>
 * @author
 * @version 1.0
 * history:
 * Date:          Resp            Comment
 * 2011-01-14     Selle Wu        Modify
 */

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	// private static Logger logger = (Logger) LogFactory.getLog( DateUtil.class
	// );

	public DateUtil() {
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 * @throws UtilDateException
	 */
	public static java.sql.Date getCurrentSqlDate() {
		try {
			java.util.Date utilNow = new java.util.Date();
			java.sql.Date sqlNow = new java.sql.Date(utilNow.getTime());
			return sqlNow;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 * @throws UtilDateException
	 */
	public static java.util.Date parseDate(java.sql.Date sqlDate) {
		try {
			java.util.Date utilNow = new java.util.Date(sqlDate.getTime());
			return utilNow;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static java.util.Date getCurrentDate() {
		try {
			java.util.Date utilNow = new java.util.Date();
			return utilNow;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 * @throws UtilDateException
	 */
	public static java.sql.Timestamp getCurrentSqlTimestamp() {
		try {
			java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
			return timestamp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取系统当前时间，格式为1970110时开始的毫秒
	 * 
	 * @return
	 * @throws UtilDateException
	 */
	public static long getCurrentMillisecond() {
		return System.currentTimeMillis();
	}

	/**
	 * 时间格式转换，将毫秒数转换为字符串型时间
	 * 
	 * @param date
	 *            毫秒
	 * @param format
	 *            格式
	 * @return 字符串型时间
	 */
	public static String dateFormat(long date, String format) {
		try {
			// date = subTimeZoneOffset(date);
			if ((format == null) || (format.equals("")))
				return new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date(date));
			else
				return new SimpleDateFormat(format).format(new java.util.Date(date));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 时间格式转换，将字符串型时间转换为毫秒数 "yyyy-MM-dd HH:mm:ss" => "12345" 19 "yyyyMMddHHmmss"
	 * => "12345" 14 "yyyy-MM-dd" => "12345" 10 返回 0 是格式不
	 * 
	 * @param dateStr
	 * @return
	 */
	public static long dateFormat(String dateStr) {
		dateStr = dateStr.trim();
		if (dateStr.length() == 19) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5, 7)) - 1, Integer.parseInt(dateStr.substring(8, 10)), Integer.parseInt(dateStr.substring(11, 13)), Integer.parseInt(dateStr.substring(14, 16)), Integer.parseInt(dateStr.substring(17, 19)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}

		} else if (dateStr.length() == 14) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(4, 6)) - 1, Integer.parseInt(dateStr.substring(6, 8)), Integer.parseInt(dateStr.substring(8, 10)), Integer.parseInt(dateStr.substring(10, 12)), Integer.parseInt(dateStr.substring(12, 14)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else if (dateStr.length() == 10) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5, 7)) - 1, Integer.parseInt(dateStr.substring(8, 10)), 0, 0, 0);
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else {
			return 0;
		}

	}

	/**
	 * 时间格式转换，将字符串型时间转换为毫秒数 "yyyy-MM-dd HH:mm:ss" => "12345" 19 "yyyyMMddHHmmss"
	 * => "12345" 14 "yyyy-MM-dd" => "12345" 10 返回 0 是格式不
	 * 
	 * @param dateStr
	 * @return
	 */
	public static long parseStringToLong(String dateStr) {
		dateStr = dateStr.trim();
		if (dateStr.length() == 19) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5, 7)) - 1, Integer.parseInt(dateStr.substring(8, 10)), Integer.parseInt(dateStr.substring(11, 13)), Integer.parseInt(dateStr.substring(14, 16)), Integer.parseInt(dateStr.substring(17, 19)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}

		} else if (dateStr.length() == 14) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(4, 6)) - 1, Integer.parseInt(dateStr.substring(6, 8)), Integer.parseInt(dateStr.substring(8, 10)), Integer.parseInt(dateStr.substring(10, 12)), Integer.parseInt(dateStr.substring(12, 14)));
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else if (dateStr.length() == 10) {
			try {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(Integer.parseInt(dateStr.substring(0, 4)), Integer.parseInt(dateStr.substring(5, 7)) - 1, Integer.parseInt(dateStr.substring(8, 10)), 0, 0, 0);
				cal.set(java.util.Calendar.MILLISECOND, 0);
				return (cal.getTime().getTime());
			} catch (Exception e) {
				return 0;
			}
		} else {
			try {
				return Long.parseLong(dateStr);
			} catch (Exception e) {
				return 0;
			}

		}

	}

	/**
	 * 转换时间 long -> String
	 * 
	 * @param timestamp
	 *            传入系统时间
	 * @return String
	 */
	public static String parseLongToString(long timestamp) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String aa = null;
		try {
			java.util.Date date = new java.util.Date(timestamp);
			aa = bartDateFormat.format(date);
		} catch (Exception ex) {
			// logger.error( ex.getMessage() );
		}
		return aa;
	}

	/**
	 * 转换时间 long -> String 时间格式：yyyy-MM-dd
	 * 
	 * @param timestamp
	 *            传入系统时间
	 * @return String
	 */
	public static String parseLongToString1(long timestamp) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String aa = null;
		try {
			java.util.Date date = new java.util.Date(timestamp);
			aa = bartDateFormat.format(date);
		} catch (Exception ex) {
			// logger.error( ex.getMessage() );
		}
		return aa;
	}

	public static java.util.Date getDate(String name, String df, java.util.Date defaultValue) {
		if (name == null) {
			return defaultValue;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(df);

		try {
			if (name.length() < 11) {
				name += " 00:00:00";
			}
			if (name.indexOf("/") > -1) {
				name = StringUtils.replace(name, "/", "-");
			}
			return formatter.parse(name);
		} catch (ParseException e) {

		}

		return defaultValue;
	}

	public static java.util.Date getDateDf(String name, String df) {
		return getDate(name, df, null);
	}

	public static java.util.Date getDate(String name, String df) {
		return getDate(name, df, null);
	}

	public static java.util.Date getDate(String name) {
		return getDate(name, "yyyy-MM-dd", null);
	}

	public static java.util.Date getMonth(String name) {
		return getDate(name, "yyyy-MM", null);
	}

	public static java.util.Date getDateTime(String name) {
		return getDateTime(name, null);
	}

	public static java.util.Date getDate(String name, java.util.Date defaultValue) {
		return getDate(name, "yyyy-MM-dd", defaultValue);
	}

	public static java.util.Date getDateTime(String name, java.util.Date defaultValue) {
		return getDate(name, "yyyy-MM-dd HH:mm:ss", defaultValue);
	}

	/**
	 * 时间格式转换，将毫秒数转换为字符串型时间
	 * 
	 * @param date
	 *            java.util.Date
	 * @param format
	 *            格式
	 * @return 字符串型时间
	 */
	public static String dateFormat(java.util.Date date, String format) {
		try {
			// date = subTimeZoneOffset(date);
			if ((format == null) || (format.equals("")))
				return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
			else
				return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static java.sql.Timestamp parseTimestamp(java.util.Date utilDate) {
		try {
			Timestamp timestamp = new Timestamp(utilDate.getTime());

			return timestamp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断当前时间是否比oldTime晚second秒,如果是返回true,否则返回false
	 * 
	 * @param oldTime
	 * @param second
	 * @return boolean
	 */
	public static boolean timeToJudge(String oldTime, int second) {
		// 把秒设置成毫秒
		long millisecond = second * 1000;
		// 当前时间和原始时间的差毫秒
		long poorMs = System.currentTimeMillis() - dateFormat(oldTime);
		// 如果当前时间和原始时间查毫秒比millisecond大，返回true
		if (poorMs >= millisecond) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param second
	 * @return
	 */
	public static Map<String, String> getStartAndEnd(int second) {
		// 把秒设置成毫秒
		long millisecond = second * 1000;
		long start = System.currentTimeMillis();
		long end = start + millisecond;
		Map<String, String> map = new HashMap<String, String>();
		map.put("start", dateFormat(start, "yyyyMMddHHmmss"));
		map.put("end", dateFormat(end, "yyyyMMddHHmmss"));
		return map;
	}

	public static String getCurDtStr() {
		Date nowTime = new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
		return sdFormatter.format(nowTime);
	}

	public static String getCurDtStr(String datePtn) {
		Date nowTime = new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter = new SimpleDateFormat(datePtn);
		return sdFormatter.format(nowTime);
	}

	/**
	 * 获取系统当前时间，格式是yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getCurDtsStr() {
		Date nowTime = new Date(System.currentTimeMillis());
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdFormatter.format(nowTime);
	}

	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static String getCurDtWeekStr() {
		Date nowTime = new Date(System.currentTimeMillis());
		return getWeekOfDate(nowTime);
	}

	public static String stringDateFormat(String date) {
		String reString = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		return reString;
	}

	/**
	 * 获得有中文的当天日期(2014年5月22日)
	 * 
	 * @return
	 */
	public static String getChineseFormatDate() {
		String today = getCurDtStr();
		String chieseDate = today.substring(0, 4);
		// System.out.println(today.substring(6, 7));
		if ("0".equals(today.substring(4, 5))) {
			chieseDate += "年" + today.substring(5, 6);
		} else {
			chieseDate += "年" + today.substring(4, 6);
		}
		if ("0".equals(today.substring(6, 7))) {
			chieseDate += "月" + today.substring(7, 8);
		} else {
			chieseDate += "月" + today.substring(6, 8);
		}
		return chieseDate + "日";
	}

	/**
	 * 获得有中文的当天日期(2014年5月22日)
	 * 
	 * @return
	 */
	public static String getChineseFormatDate(String dts) {
		String chieseDate = dts.substring(0, 4);
		if ("0".equals(dts.substring(4, 5))) {
			chieseDate += "年" + dts.substring(5, 6);
		} else {
			chieseDate += "年" + dts.substring(4, 6);
		}
		if ("0".equals(dts.substring(6, 7))) {
			chieseDate += "月" + dts.substring(7, 8);
		} else {
			chieseDate += "月" + dts.substring(6, 8);
		}
		return chieseDate + "日";
	}

	public static String getDtStrByInterval(String format, int unit, int interval) {
		Calendar cal = Calendar.getInstance();
		cal.add(unit, interval);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String rslt = sdf.format(cal.getTime());
		return rslt;
	}

	/**
	 * 获得从startDt到endDt时间的差(0天18小时0分)
	 * 
	 * @param startDt
	 * @param endDt
	 * @return
	 */
	public static String getTimeDifferenceDHM(String startDt, String endDt) {
		String diffStr = null;
		if (StringUtils.isNotBlank(startDt) && StringUtils.isNotBlank(endDt)) {
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
			long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
			long nh = 1000 * 60 * 60;// 一小时的毫秒数
			long nm = 1000 * 60;// 一分钟的毫秒数
			long ns = 1000;// 一秒钟的毫秒数
			long diff;
			try {
				diff = sd.parse(endDt).getTime() - sd.parse(startDt).getTime();
				long day = diff / nd;// 计算差多少天
				long hour = diff % nd / nh;// 计算差多少小时
				long min = diff % nd % nh / nm;// 计算差多少分钟
				// long sec = diff%nd%nh%nm/ns;//计算差多少秒
				// diffStr = day+"天"+hour+"小时"+min+"分钟"+sec+"秒。";
				diffStr = day + "天" + hour + "小时" + min + "分钟";
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return diffStr;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String startDt, String endDt) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		Long diff = 0L;
		if (StringUtils.isNotBlank(startDt) && StringUtils.isNotBlank(endDt)) {
			try {
				if (StringUtils.length(startDt) == 8) {
					startDt = startDt + "000000";
				}
				if (StringUtils.length(endDt) == 8) {
					endDt = endDt + "000000";
				}
				diff = sd.parse(endDt).getTime() - sd.parse(startDt).getTime();
				long day = diff / nd;// 计算差多少天
				// long hour = diff % nd / nh;//计算差多少小时
				// long min = diff % nd % nh / nm;//计算差多少分钟
				// //long sec = diff%nd%nh%nm/ns;//计算差多少秒
				// //diffStr = day+"天"+hour+"小时"+min+"分钟"+sec+"秒。";
				// diffStr = day + "天" + hour + "小时" + min + "分钟";
				diff = day;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			return 0;
		}
		return diff.intValue();
	}

	public static String getDtStrByInterval(String fromDt, String format, int unit, int interval) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(fromDt));
			cal.add(unit, interval);
			String rslt = sdf.format(cal.getTime());
			return rslt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getDtStrByIntervalForRedEnvelope(int unit, int interval) {
		String format = "yyyyMMdd";
		Calendar cal = Calendar.getInstance();
		cal.add(unit, interval);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String rslt = sdf.format(cal.getTime()) + "235959";
		return rslt;
	}

	/**
	 * 获得指定日期N天前(-)/后(+)的日期时间 格式 yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String getNDaysDateTime(Date nowDate, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		cal.add(Calendar.DATE, days);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
	}

	/**
	 * 
	 * @param oldTime
	 * @return
	 */
	public static boolean compareDates(String time1, String time2) {
		if (dateFormat(time1) >= dateFormat(time2)) {
			return true;
		} else {
			return false;
		}
	}

	public static String addDate(int period, String unit) {
		String now = getCurDtsStr();
		return addDate(now, period, unit);
	}

	public static String addDate(String start, int period, String unit) {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date startDate1 = f.parse(start);

			Calendar starCal = Calendar.getInstance();
			starCal.setTime(startDate1);
			if ("D".equals(unit)) {
				starCal.add(Calendar.DATE, period);
			} else if ("M".equals(unit)) {
				starCal.add(Calendar.MONTH, period);
			} else {
				starCal.add(Calendar.YEAR, period);
			}
			return f.format(starCal.getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.HOUR, -1);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		// String rslt = sdf.format(cal.getTime());
		// System.out.println(getDtStrByInterval("yyyyMMdd", Calendar.MONTH,
		// 3));
		// System.out.println("123456789".substring(0, 8));
		try {
			// System.out.println(getNDaysDateTime(new Date(),30));
			// System.out.println(getDtStrByInterval("20120531","yyyyMMdd",Calendar.MONTH,
			// 1).substring(0, 6));
			// System.out.println("20150128".substring(6, 8));

			System.out.println(parseLongToString(1492534647) + "-- "+ parseLongToString(1491969600));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
