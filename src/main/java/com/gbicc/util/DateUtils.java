package com.gbicc.util;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils extends PropertyEditorSupport {

	public static final String[] DATE_PATTERNS = new String[]{"yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss"};


	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private boolean emptyAsNull;

	private String dateFormat = DEFAULT_DATE_FORMAT;

	public DateUtils(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public DateUtils(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return value != null ? new SimpleDateFormat(dateFormat).format(value) : "";
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String value = text.trim();
			if (emptyAsNull && "".equals(value)) {
				setValue(null);
			} else {
				try {
					setValue(org.apache.commons.lang.time.DateUtils.parseDate(value, DATE_PATTERNS));
				} catch (ParseException e) {
					setValue(null);
				}
			}
		}
	}

	public static String getStringFromDate(Date date, String formatstring) {
		try {
			DateFormat format = new SimpleDateFormat(formatstring);
			return date == null ? null : format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String fromDate(Date date, String pattern) {
		if (pattern != null) {
			sdf.applyPattern(pattern);
		}
		if (date == null) {
			return null;
		}

		return sdf.format(date);
	}

	public static String fromDate(Date date) {
		String pattern = DEFAULT_DATE_FORMAT;

		if (pattern != null) {
			sdf.applyPattern(pattern);
		}
		if (date == null) {
			return null;
		}

		return sdf.format(date);
	}

	/**
	 * @param
	 * @Description: 获取两个时间之间相差的分钟数
	 * @author 441889070@qq.com
	 * @date 2015年11月29日 上午9:32:19
	 */
	public static Long dateDiffMinute(Date startDate, Date endDate) {
		//一天的毫秒数
		long nd = 1000 * 24 * 60 * 60;
		//一小时的毫秒数
		long nh = 1000 * 60 * 60;
		//一分钟的毫秒数
		long nm = 1000 * 60;
		long diff = endDate.getTime() - startDate.getTime();
		Long diff_min = (Long) (diff / nm);
		//计算差多少分钟
		long min = diff_min;
		return min;
	}


	/**
	 * author		:		dengwenbing
	 * describe		:		计算日期的一号
	 */
	public static Date getMonthFirstDay(Date time) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(time);
		c.add(Calendar.DAY_OF_MONTH, 1 - c.get(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * author		:		dengwenbing
	 * describe		:		计算星期的礼拜一
	 */
	public static Date getWeekFirstDay(Date time) {
		Calendar date = Calendar.getInstance(Locale.CHINA);
		date.setTime(time);
		date.setFirstDayOfWeek(Calendar.MONDAY);
		date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return date.getTime();
	}

	/**
	 * @param
	 * @Description: 获取一天的开始时间
	 * @author 441889070@qq.com
	 * @date 2015年12月4日 下午12:23:08
	 */
	public static Date getDayStart(Date time) {
		String dateStr = fromDate(time, "yyyy-MM-dd");
		dateStr += " 00:00:00";
		return strToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @param
	 * @Description: 获取一天的结束时间
	 * @author 441889070@qq.com
	 * @date 2015年12月4日 下午12:23:27
	 */
	public static Date getDayEnd(Date time) {
		String dateStr = fromDate(time, "yyyy-MM-dd");
		dateStr += " 23:59:59";
		return strToDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取昨天日期
	 *
	 * @param
	 * @Description: TODO
	 * @author 441889070@qq.com
	 * @date 2015年12月4日 下午12:28:28
	 */
	public static Date getYesterday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.getTime();
	}

	public static Date getTomorrow(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}


	public static Date getDateAfter(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTime();
	}

	public static Date getDateAfterByMinute(Date date, int minute) {
		Long dateNew = date.getTime() + minute * 60 * 1000;
		return new Date(dateNew);
	}

	public static Date strToDate(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateResut = null;
		try {
			dateResut = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateResut;
	}

	public static Date strToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		Date dateResut = null;
		try {
			dateResut = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateResut;
	}

	public static void main(String[] args) {
		System.out.println(String.format("%08d", 11));

	}


	public static int getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获取当前时间的一小时以前
	 *
	 * @return
	 */
	public static String getOneHourAgoTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(calendar.getTime());
	}


	/**
	 * 获取几分钟前或者几分钟之后的时间
	 *
	 * @param minute
	 * @return
	 */
	public static String getTimeByMinute(int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, minute);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

	/**
	 * @param date1  字符串日期1
	 * @param date2  字符串日期2
	 * @param format 日期格式化方式  format="yyyy-MM-dd"
	 * @return
	 * @descript:计算两个字符串日期相差的天数
	 */
	public static long dayDiff(String date1, String date2, String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		long diff = 0L;
		try {
			long d1 = formater.parse(date1).getTime();
			long d2 = formater.parse(date2).getTime();
			diff = (d1 - d2) / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
	}

	/**
	 * 字符串转换成日期
	 *
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期换字符串
	 *
	 * @param date
	 * @return date
	 */
	public static String DateToString(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String dateStr = format.format(date);
			return dateStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串转换成日期
	 *
	 * @param str
	 * @return date
	 */
	public static Date StrToDateToDday(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


	/**
	 * 日期换字符串
	 *
	 * @param date
	 * @return date
	 */
	public static String DateToStringDay(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String dateStr = format.format(date);
			return dateStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static Date retMonthFirstDay(Date date, String month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDayOfMonth = calendar.getTime();
		return firstDayOfMonth;
	}

	public static Date retMonthLastDay(Date date, String month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDayOfMonth = calendar.getTime();
		return lastDayOfMonth;
	}

	public static Date getDateOfAddDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//把日期往后增加一天.整数往后推,负数往前移动
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}


	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate){
		try {
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
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate){

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));

		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取两个时间的时间差
	 * @param date1 开始时间
	 * @param date2 结束时间
	 * @return
	 */
	public static String getTimeDifference(Date date1,Date date2){
		Long beginL = date1.getTime();
		Long endL = date2.getTime();

		long diff =0;

		if(endL>beginL){
			diff = endL - beginL;
		}else{
			diff = beginL - endL;
		}
		long day = diff/(24*60*60*1000);
		long hour =  diff/(60*60*1000) - day*24;
		long min = diff/(60*1000) - day*24*60 - hour*60;
		long second =  diff/1000-day*24*60*60-hour*60*60-min*60;

		String remainingTime = "";
		if(day>0){
			remainingTime = remainingTime + day +"天";
		}
		if(hour>0){
			remainingTime = remainingTime + hour ;
		}else{
			remainingTime = remainingTime + "00" ;
		}
		if(min>0){
			remainingTime = remainingTime + ":"+ min ;
		}else{
			remainingTime = remainingTime + ":"+ "00" ;
		}
		if(second>0){
			remainingTime = remainingTime + ":"+ second ;
		}else{
			remainingTime = remainingTime + ":"+ "00" ;
		}
		return remainingTime;
	}


}