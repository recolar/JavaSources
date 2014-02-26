package com.rec.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.rec.exception.RecRunTimeException;

/**
 * 日期操作类
 * 
 * @author Recolar
 * @version 1.0.1 2006-12-30
 * @see
 * @since JDK1.5.0
 */
public final class RecDate implements java.io.Serializable, Cloneable, Comparable<Object> {

	/**
	 * 日历对象,内部始终维持此对象作为主控类
	 */
	private Calendar calendar = Calendar.getInstance();

	/**
	 * 格式化日期时间的字符串
	 */
	private static final String DATETIME_STR_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 格式化日期的字符串
	 */

	private static final String DATE_STR_PATTERN = "yyyy-MM-dd";

	/**
	 * 日期格式化对象,格式如:"2008-01-31" private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * /** 日期格式化对象,格式如:"2008-01-31 12:12:05"
	 */
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(DATETIME_STR_PATTERN);// 日期时间格式化对象

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_STR_PATTERN);// 日期格式化对象

	private static final long serialVersionUID = 1L;

	/**
	 * 设置每个星期的第一天是星期几，一为1，日为0。默认为星期日
	 */
	private int weekFirstDay = 0;

	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;
	public static final int SATURDAY = 6;

	/**
	 * 默认构造函数<br>
	 */
	public RecDate() {
		super();
	}

	/**
	 * 用日期对象构造此对象
	 * 
	 * @param date util包下的日期对象
	 */
	public RecDate(Date date) {
		this.calendar.setTimeInMillis(date.getTime());
	}

	/**
	 * 用年,月,日构造日期对象
	 * 
	 * @param year 年份
	 * @param month 月份
	 * @param date 日期
	 */
	public RecDate(int year, int month, int date) {
		this("" + year + "-" + month + "-" + date);
	}

	/**
	 * 用年,月,日,时,分,秒构造日期对象<br>
	 * 
	 * @param year 年份
	 * @param month 月份
	 * @param date 日期
	 * @param hours 时
	 * @param minutes 分
	 * @param seconds 秒
	 */
	public RecDate(int year, int month, int date, int hours, int minutes, int seconds) {
		this.calendar.set(year, month - 1, date, hours, minutes, seconds);

	}

	/**
	 * 用java.sql.Date构造RecDate对象
	 * 
	 * @param date java.sql.Date
	 */
	public RecDate(java.sql.Date date) {
		this.calendar.setTimeInMillis(date.getTime());

	}

	/**
	 * 用long类型(从1970年)经过的毫秒数构造RecDate对象
	 * 
	 * @param times 毫秒数
	 */
	public RecDate(long times) {
		this.calendar.setTimeInMillis(times);

	}

	/**
	 * 用字符串初始化日期值
	 * 
	 * @param dateTime 时间日期的字符串,格式如:2006-12-12或者2006-12-12 08:15:23
	 */
	public RecDate(String dateTime) {
		dateTime = dateTime.replaceAll("\\D", "-");
		if ((dateTime.indexOf("-") == -1) || (dateTime.split("-").length != 3)) {
			this.calendar = Calendar.getInstance();
		} else {
			String[] ymd = dateTime.split("-");
			int year = Integer.parseInt(ymd[0]);
			int month = Integer.parseInt(ymd[1]) - 1;
			int date = Integer.parseInt(ymd[2]);
			this.calendar.set(year, month, date, 0, 0, 0);
		}
		// try {
		// this.calendar.setTime(this.dateTimeFormatter.parse(dateTime));
		// } catch (ParseException e) {
		// try {
		// this.calendar.setTime(this.dateTimeFormatter.parse(dateTime));
		// } catch (ParseException e1) {
		// this.calendar = Calendar.getInstance();
		// }
		// } finally {
		// }
	}

	/**
	 * 用java.sql.Timestamp构造RecDate对象
	 * 
	 * @param date java.sql.Timestamp
	 */
	public RecDate(Timestamp date) {
		this.calendar.setTimeInMillis(date.getTime());
	}

	/**
	 * 在当前日期日的基础上加上一个数字
	 * 
	 * @param addNum 增量,如为负数即减去一个数值
	 */
	public RecDate addDates(int addNum) {
		this.calendar.add(Calendar.DATE, addNum);
		return this;
	}

	/**
	 * 在当前日期的小时的基础上加上一个小时的数字
	 * 
	 * @param addNum 增量,如为负数即减去一个小时数值
	 */
	public RecDate addHours(int addNum) {
		this.calendar.add(Calendar.HOUR, addNum);
		return this;
	}

	/**
	 * 在当前日期的分钟的基础上加上一个分钟的数字
	 * 
	 * @param addNum 增量,如为负数即减去一个分钟数值
	 */
	public RecDate addMinutes(int addNum) {
		this.calendar.add(Calendar.MINUTE, addNum);
		return this;
	}

	/**
	 * 在当前日期的月份的基础上加上一个月份的数字
	 * 
	 * @param addNum 增量,如为负数即减去一个月份数值
	 */
	public RecDate addMonths(int addNum) {
		this.calendar.add(Calendar.MONDAY, addNum);
		return this;
	}

	/**
	 * 在当前日期的秒数的基础上加上一个秒数的数字
	 * 
	 * @param addNum 增量,如为负数即减去一个秒数数值
	 */
	public RecDate addSeconds(int addNum) {
		this.calendar.add(Calendar.SECOND, addNum);
		return this;
	}

	/**
	 * 在当前日期的年份的基础上加上一个年份的数字
	 * 
	 * @param addNum 增量,如为负数即减去一个年份数值
	 */
	public RecDate addYears(int addNum) {
		this.calendar.add(Calendar.YEAR, addNum);
		return this;
	}

	/**
	 * 卡隆一个当前实例,深拷贝
	 */
	@Override
	public Object clone() {
		RecDate copyDate = null;
		try {
			copyDate = (RecDate) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		copyDate.calendar = (Calendar) this.calendar.clone();
		copyDate.dateTimeFormatter = (SimpleDateFormat) this.dateTimeFormatter.clone();
		return copyDate;
	}

	public int compareTo(Object anotherDate) {
		long thisTime = 0, anotherTime = 0;
		if (anotherDate instanceof Date) {
			thisTime = getTime();
			anotherTime = ((Date) anotherDate).getTime();
		} else if (anotherDate instanceof java.sql.Date) {
			thisTime = getTime();
			anotherTime = ((java.sql.Date) anotherDate).getTime();
		} else if (anotherDate instanceof Calendar) {
			thisTime = getTime();
			anotherTime = ((Calendar) anotherDate).getTimeInMillis();
		} else if (anotherDate instanceof RecDate) {
			thisTime = getTime();
			anotherTime = ((RecDate) anotherDate).getTime();
		} else {
			throw new RecRunTimeException("所提供参数不是日期相关对象");
		}
		return thisTime > anotherTime ? 1 : (thisTime < anotherTime ? -1 : 0);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((calendar == null) ? 0 : calendar.hashCode());
		// result = PRIME * result + ((dateFormatter == null) ? 0 : dateFormatter.hashCode());
		result = PRIME * result + ((dateTimeFormatter == null) ? 0 : dateTimeFormatter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof RecDate) {
			RecDate td = (RecDate) obj;
			return td.getTime() == getTime();
		}
		return false;
	}

	/**
	 * 如果大于date日期变量的时间值返回1的数<br>
	 * 如果小于date日期变量的时间值返回-1的数<br>
	 * 如果等于date日期变量的时间值返回0的数<br>
	 * 
	 * @param date 需要参与比较的Date日期变量
	 * @return RecDate变量和Date参数变量比较结果值
	 */
	public int compareToDate(Date date) {
		return this.calendar.getTime().getTime() == date.getTime() ? 0 : (this.calendar.getTime().getTime() > date
				.getTime() ? 1 : -1);
	}

	/**
	 * 与字符串时间的时间值对比,如果大于返回1等于返回0小于返回-1<br>
	 * 
	 * @param date String日期变量
	 * @return 对比值
	 */
	public int compareToDate(String date) {
		if (!RecDate.checkDataIsValid(date)) {
			throw new RecRunTimeException("所提供的日期格式不正确，应为yyyy-mm-dd或者yyyy-MM-dd HH:mm:ss的格式形式");
		}
		RecDate rd = new RecDate(date);// 参数值
		return getTime() == rd.getTime() ? 0 : (getTime() > rd.getTime() ? 1 : -1);
	}

	/**
	 * 获取当前日期<br>
	 * 
	 * @return 当前日期
	 */
	public int getDate() {
		return this.calendar.get(Calendar.DATE);
	}

	/**
	 * 获取与参数日期之间相隔的天数
	 * 
	 * @param date Date对象
	 * @return 相隔的天数(返回以绝对值)
	 */
	public long getDateInteval(Date date) {
		return Math.abs((long) (getTime() - date.getTime()) / (86400 * 1000));
	}

	/**
	 * 获取与参数日期之间相隔的天数
	 * 
	 * @param date RecDate对象
	 * @return 相隔的天数(返回以绝对值)
	 */
	public long getDateInteval(RecDate date) {
		return getDateInteval(date.getDateObject());
	}

	/**
	 * 获取与参数字符串日期之间相隔的天数
	 * 
	 * @param date 字符串日期所表示的String对象
	 * @return 相隔的天数
	 */
	public long getDateInteval(String date) {
		if (!RecDate.checkDataIsValid(date)) {
			throw new RecRunTimeException("参数日期不合法,请检查字符串日期参数格式是否正确");
		}
		return getDateInteval(RecDate.getInstance(date).getDateObject());
	}

	/**
	 * 两个日期对象年份相隔数<br>
	 * 不足1年的，按0来算
	 * 
	 * @param date
	 * @return
	 */
	public int getYearInteval(RecDate date) {
		return (int) Math.abs(getDateInteval(date) / 365);
	}

	/**
	 * 返回一个java.util.Date对象<br>
	 * 
	 * @return Date对象
	 */
	public Date getDateObject() {
		return (Date) this.calendar.getTime().clone();
	}

	/**
	 * 返回字符串日期<br>
	 * 
	 * @return 字符串日期
	 */
	public String getDateString() {
		return this.dateFormatter.format(this.calendar.getTime());
	}

	/**
	 * 根据所提供的格式日期字符串来格式一个时间
	 * 
	 * @param format 格式化日期的字符串
	 * @return String 格式化字符串
	 */
	public String getFormatDateTimeString(String format) {
		return new SimpleDateFormat(format).format(this.calendar.getTime());
	}

	/**
	 * 返回字符串日期时间<br>
	 * 
	 * @return 字符串日期时间
	 */
	public String getDateTimeString() {
		return this.dateTimeFormatter.format(this.calendar.getTime());
	}

	/**
	 * 获取当前日期为星期几,1为星期一,0为星期日
	 * 
	 * @return 星期几的值
	 */
	public int getDay() {
		return this.calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获取小时<br>
	 * 
	 * @return 小时
	 */
	public int getHours() {
		return this.calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取本地年月日格式
	 * 
	 * @return String
	 */
	public String getChinaDateString() {
		StringBuffer str = new StringBuffer();
		str.append(getYear()).append("年");
		str.append(getMonth()).append("月");
		str.append(getDate()).append("日");
		return str.toString();
	}

	public String getChinaDateTimeString() {
		StringBuffer str = new StringBuffer();
		str.append(getYear()).append("年");
		str.append(getMonth()).append("月");
		str.append(getDate()).append("日");
		str.append(" ").append(getHours()).append("时");
		str.append(getMinutes()).append("分");
		str.append(getSeconds()).append("秒");
		return str.toString();
	}

	public String getDayStr() {
		String dayStr = "";
		int day = getDay();
		if (day == 1) {
			dayStr = "一";
		} else if (day == 2) {
			dayStr = "二";
		} else if (day == 3) {
			dayStr = "三";
		} else if (day == 4) {
			dayStr = "四";
		} else if (day == 5) {
			dayStr = "五";
		} else if (day == 6) {
			dayStr = "六";
		} else if (day == 0) {
			dayStr = "日";
		}
		return dayStr;
	}

	/**
	 * 返回星期几
	 * 
	 * @return String
	 */
	public String getLocalWeekDayString() {
		return "星期" + getDayStr();
	}

	/**
	 * 返回格式如：2012年3月4日【星期三】
	 * 
	 * @return
	 */
	public String getLocalDateAndWeekDateString() {
		return getChinaDateString() + "【" + getLocalWeekDayString() + "】";
	}

	/**
	 * 获取当前日期在当前年的第几天
	 * 
	 * @return int
	 */
	public int getDateOfYear() {
		return this.calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取当前日期是当前月份所在的第几个星期
	 * 
	 * @return int
	 */
	public int getWeekOfMonth() {
		return this.calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取当前日期是当前年份所在的第几个星期
	 * 
	 * @return int
	 */
	public int getWeekOfYear() {
		return this.calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取分钟<br>
	 * 
	 * @return 分钟
	 */
	public int getMinutes() {
		return this.calendar.get(Calendar.MINUTE);
	}

	/**
	 * 获取月份<br>
	 * 
	 * @return 月份
	 */
	public int getMonth() {
		return this.calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 求得当月的第一天<br>
	 * 
	 * @return 当前月份的第一天
	 */
	public String getMonthFirstDate() {
		int month = getMonth();
		return getYear() + "-" + (month < 10 ? ("0" + month) : String.valueOf(month)) + "-01";
	}

	/**
	 * 求得当前月的最后一天<br>
	 * 
	 * @return 当前月份的最后一天
	 */
	public String getMonthLastDate() {
		Calendar cld = (Calendar) this.calendar.clone();
		cld.set(getYear(), getMonth(), 1);
		cld.add(Calendar.DATE, -1);
		return this.dateTimeFormatter.format(cld.getTime());
	}

	/**
	 * 设置每个星期的第一天是星期几,星期一为1，星期日为0。默认为星期日<br>
	 * 可以通过RecDate.MONDAY等等方式赋值
	 * 
	 * @param day
	 */
	public RecDate setWeekFirstDate(int day) {
		this.weekFirstDay = Math.abs(day) % 7;
		return this;
	}

	/**
	 * 获取当前周的第一天
	 * 
	 * @return
	 */
	public RecDate getWeekFirstDate() {
		Calendar cld = (Calendar) this.calendar.clone();
		int dayOfWeek = getDay();
		cld.add(Calendar.DATE, (0 - dayOfWeek - (7 - this.weekFirstDay)) % 7);
		return new RecDate(cld.getTime());
	}

	/**
	 * 获取当前周的最后一天
	 * 
	 * @return
	 */
	public RecDate getWeekLastDate() {
		return getWeekFirstDate().addDates(6);
	}

	/**
	 * 获取秒数<br>
	 * 
	 * @return 描述
	 */
	public int getSeconds() {
		return this.calendar.get(Calendar.SECOND);
	}

	/**
	 * 返回java.sql.Date对象<br>
	 * 
	 * @return java.sql.Date对象
	 */
	public java.sql.Date getSqlDateObject() {
		return new java.sql.Date(this.calendar.getTimeInMillis());
	}

	/**
	 * 获取1970-01-01零时到现在所经过的毫秒数<br>
	 * 
	 * @return 毫秒数目
	 */
	public long getTime() {
		return this.calendar.getTimeInMillis();
	}

	/**
	 * 返回java.sql.Timestamp对象<br>
	 * 
	 * @return java.sql.Timestamp对象
	 */
	public Timestamp getTimestampObject() {
		return new Timestamp(this.calendar.getTimeInMillis());
	}

	/**
	 * 获取年份<br>
	 * 
	 * @return 年份
	 */
	public int getYear() {
		return this.calendar.get(Calendar.YEAR);
	}

	/**
	 * 如果当前日期晚于参数中的日期则返回true否则返回false
	 * 
	 * @param tempDate 比较的日期参数
	 * @return boolean
	 */
	public boolean isAfter(Date tempDate) {
		return getTime() > tempDate.getTime();
	}

	/**
	 * 如果当前日期晚于参数中的毫秒数说构成的日期值的话则返回true否则返回false
	 * 
	 * @param timesmillis 从1970-1-1零时所经过的毫秒数
	 * @return boolean
	 */
	public boolean isAfter(long timesmillis) {
		return getTime() > timesmillis;
	}

	/**
	 * 如果当前日期晚于参数中的日期则返回true否则返回false
	 * 
	 * @param tempDate 比较的日期参数
	 * @return boolean
	 */
	public boolean isAfter(RecDate tempDate) {
		return getTime() > tempDate.getTime();
	}

	/**
	 * 如果当前日期早于参数中的日期则返回true否则返回false
	 * 
	 * @param tempDate 比较的日期参数
	 * @return boolean
	 */
	public boolean isBefore(Date tempDate) {
		return getTime() < tempDate.getTime();
	}

	/**
	 * 如果当前日期早于参数中的毫秒数说构成的日期值的话则返回true否则返回false
	 * 
	 * @param timesmillis 从1970-1-1零时所经过的毫秒数
	 * @return boolean
	 */
	public boolean isBefore(long timesmillis) {
		return getTime() < timesmillis;
	}

	/**
	 * 如果当前日期早于参数中的日期则返回true否则返回false
	 * 
	 * @param tempDate 比较的日期参数
	 * @return boolean
	 */
	public boolean isBefore(RecDate tempDate) {
		return getTime() < tempDate.getTime();
	}

	/**
	 * 如果该日期对象是年月日格式的时候返回true否则返回false
	 * 
	 * @return boolean
	 */
	public boolean isDateType() {
		return (getHours() == 0 && getMinutes() == 0 && getSeconds() == 0);
	}

	/**
	 * 用日期对象设置日期
	 * 
	 * @param date util包下日期对象
	 */
	public void setDate(Date date) {
		this.calendar.setTimeInMillis(date.getTime());

	}

	/**
	 * 设置日期,如果日期大于31或者小于0则参数无效
	 * 
	 * @param date 日期
	 */
	public RecDate setDate(int date) {
		this.calendar.set(Calendar.DATE, date);
		return this;
	}

	/**
	 * 用日期对象设置日期
	 * 
	 * @param date sql包下日期对象
	 */
	public RecDate setDate(java.sql.Date date) {
		this.calendar.setTimeInMillis(date.getTime());
		return this;
	}

	/**
	 * 用日期对象设置日期
	 * 
	 * @param date sql包下Timestamp对象
	 */
	public RecDate setDate(java.sql.Timestamp date) {
		this.calendar.setTimeInMillis(date.getTime());
		return this;
	}

	/**
	 * 设置年月日参数
	 * 
	 * @param year 年份
	 * @param month 月份
	 * @param date 日期
	 */
	public RecDate setDateParam(int year, int month, int date) {
		this.calendar.set(year, month - 1, date, 0, 0, 0);
		return this;
	}

	/**
	 * 设置年月日时分秒等参数
	 * 
	 * @param year 年份
	 * @param month 月份
	 * @param date 日期
	 * @param hours 小时
	 * @param minutes 分钟
	 * @param seconds 秒数
	 */
	public RecDate setDateTimeParam(int year, int month, int date, int hours, int minutes, int seconds) {
		this.calendar.set(year, month - 1, date, hours, minutes, seconds);
		return this;
	}

	/**
	 * 用时间日期字符串设置日期
	 * 
	 * @param dateTime 时间日期的字符串,格式如:2006-12-12或者2006-12-12 08:15:23
	 */
	public RecDate setDateTimeString(String dateTime) {
		try {
			this.calendar.setTime(this.dateTimeFormatter.parse(dateTime));
		} catch (ParseException e) {
			try {
				this.calendar.setTime(this.dateTimeFormatter.parse(dateTime));
			} catch (ParseException e1) {
			}
		}
		return this;
	}

	/**
	 * 设置小时,如果小时值大于23或者小于0则参数无效
	 * 
	 * @param hour 小时
	 */
	public RecDate setHour(int hour) {
		if (hour < 0 || hour > 23) {
			throw new RecRunTimeException("小时数只能为0到23之间的整数");
		}
		// this.calendar.set(getYear(), getMonth() - 1, getDate(), hour, getMinutes(), getSeconds());
		this.calendar.set(Calendar.HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * 设置分钟，如果分钟小于0或者大于59则参数无效
	 * 
	 * @param minute 分钟
	 */
	public RecDate setMinute(int minute) {
		if (minute < 0 || minute > 59) {
			throw new RecRunTimeException("分钟数只能是1到59之间的数值");
		}
		// this.calendar.set(getYear(), getMonth() - 1, getDate(), getHours(), minute, getSeconds());
		this.calendar.set(Calendar.MINUTE, minute);
		return this;
	}

	/**
	 * 设置月份,如果月份大于12或者小于1则参数无效
	 * 
	 * @param month 月份
	 */
	public RecDate setMonth(int month) {
		// this.calendar.set(getYear(), month - 1, getDate(), getHours(), getMinutes(), getSeconds());
		this.calendar.set(Calendar.MONDAY, month - 1);
		return this;
	}

	/**
	 * 设置分钟，如果秒数小于0或者大于59则参数无效
	 * 
	 * @param second 秒数
	 */
	public RecDate setSecond(int second) {
		if (second < 0 || second > 59) {
			throw new RecRunTimeException("秒数只能是1到59之间的数值");
		}
		// this.calendar.set(getYear(), getMonth() - 1, getDate(), getHours(), getMinutes(), second);
		this.calendar.set(Calendar.SECOND, second);
		return this;
	}

	/**
	 * 用long类型(从1970年)经过的毫秒数构造RecDate对象
	 * 
	 * @param times 毫秒数
	 */
	public RecDate setTime(long times) {
		this.calendar.setTimeInMillis(times);
		return this;
	}

	/**
	 * 设置年份
	 * 
	 * @param year 年份
	 */
	public RecDate setYear(int year) {
		// this.calendar.set(year, getMonth() - 1, getDate(), getHours(), getMinutes(), getSeconds());
		this.calendar.set(Calendar.YEAR, year);
		return this;
	}

	/**
	 * toString方法<br>
	 * 
	 * @return 当前日期时间
	 */
	@Override
	public String toString() {
		if (isDateType()) {
			return getDateString();
		}
		return getDateTimeString();
	}

	public Calendar getCalendar() {
		return (Calendar) this.calendar.clone();
	}

	/**
	 * 检测日期格式是否正确
	 * 
	 * @return true:正确 false:失败
	 */
	public static boolean checkDataIsValid(String dateStr) {
		// boolean isOk = false;
		// try {
		// new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		// isOk = true;
		// } catch (ParseException e) {
		// try {
		// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
		// isOk = true;
		// } catch (ParseException e1) {
		// // throw new RecRunTimeException("所提供的日期格式不正确，应为yyyy-mm-dd或者yyyy-MM-dd HH:mm:ss的格式形式", e1);
		// }
		// }
		// return isOk;
		return dateStr.matches("\\d{4}\\D\\d{1,2}\\D\\d{1,2}");
	}

	/**
	 * 如果date1>date2则返回1的数<br>
	 * 如果date1<date2则返回-1的数<br>
	 * 如果date1=date2则返回等于0的数<br>
	 * 
	 * @param date1 第一个Date日期变量
	 * @param date2 第二个Date日期变量
	 * @return 第一个参数(date1)和第二个参数(date2)的比较结果值
	 */
	public static int dateCompareToDate(Date date1, Date date2) {
		return date1.getTime() == date2.getTime() ? 0 : (date1.getTime() > date2.getTime() ? 1 : -1);
	}

	/**
	 * 如果date1>date2则返回1的数<br>
	 * 如果date1<date2则返回-1的数<br>
	 * 如果date1=date2则返回等于0的数<br>
	 * 
	 * @param date1 第一个Date日期变量
	 * @param date2 第二个Date日期变量
	 * @return 第一个参数(date1)和第二个参数(date2)的比较结果值
	 */
	public static int dateCompareToDate(String date1, String date2) {
		if (!RecDate.checkDataIsValid(date1)) {
			throw new RecRunTimeException("参数日期不合法,请检查字符串日期参数格式是否正确");
		}
		if (!RecDate.checkDataIsValid(date2)) {
			throw new RecRunTimeException("参数日期不合法,请检查字符串日期参数格式是否正确");
		}
		RecDate rd1 = new RecDate(date1);
		RecDate rd2 = new RecDate(date2);
		return rd1.getTime() == rd2.getTime() ? 0 : (rd1.getTime() > rd2.getTime() ? 1 : -1);
	}

	/**
	 * 获取当前时间的年月日格式字符串
	 * 
	 * @return String
	 */
	public static String getCurrentDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new java.util.Date());
	}

	/**
	 * 获取当前时间的年月日时分秒的字符串格式
	 * 
	 * @return String
	 */
	public static String getCurrentDateTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(new java.util.Date());
	}

	/**
	 * 获取两个日期之间相隔的天数
	 * 
	 * @param date1 Date日期对象
	 * @param date2 Date日期对象
	 * @return int 两个日期相隔的天数(返回以绝对值)
	 */
	public static int getDateInteval(Date date1, Date date2) {
		return Math.abs((int) ((date2.getTime() - date1.getTime()) / (86400 * 1000)));
	}

	/**
	 * 获取两个日期之间相隔的天数
	 * 
	 * @param date1 Date日期对象
	 * @param date2 Date日期对象
	 * @return int 两个日期相隔的天数(返回以绝对值),如果有某个参数的时间日期不是合法的则返回-1
	 */
	public static int getDateInteval(String date1, String date2) {
		if (!RecDate.checkDataIsValid(date1) || !RecDate.checkDataIsValid(date2)) {
			throw new RecRunTimeException("参数日期不合法,请检查字符串日期参数格式是否正确");
		}
		return Math.abs((int) ((new RecDate(date1).getTime() - new RecDate(date2).getTime()) / (86400 * 1000)));
	}

	/**
	 * 根据字符串日期返回过滤之后的字符串<br>
	 * 如2006-12-12 00:00:00.0 转换到2006-12-12<br>
	 * 
	 * @param date
	 * @return
	 */
	public static String getFilterDate(String date) {
		if (date != null && date.length() > 10) {
			return date.substring(0, 10);
		} else {
			return date;
		}
	}

	/**
	 * 获取RecDate的一个实例,时间是取当前时间
	 * 
	 * @return RecDate实例
	 */
	public static RecDate getInstance() {
		return new RecDate();
	}

	/**
	 * 根据参数获取一个RecDate实例<br>
	 * 该参数可以使String,java.util.Date,java.sql.Date,java.sql.Timestamp,Calendar
	 * 
	 * @param dateTimeStr 时间参数
	 * @return RecDate实例
	 */
	public static RecDate getInstance(Object dateTimeParam) {
		if (dateTimeParam instanceof java.util.Date) {
			return new RecDate((java.util.Date) dateTimeParam);
		} else if (dateTimeParam instanceof java.sql.Date) {
			return new RecDate((java.sql.Date) dateTimeParam);
		} else if (dateTimeParam instanceof java.sql.Timestamp) {
			return new RecDate((java.sql.Timestamp) dateTimeParam);
		} else if (dateTimeParam instanceof String) {
			return new RecDate((String) dateTimeParam);
		} else if (dateTimeParam instanceof Calendar) {
			return new RecDate(((Calendar) dateTimeParam).getTimeInMillis());
		}
		return new RecDate();
	}

	/**
	 * 获取Oracle的日期格式
	 * 
	 * @return String
	 */
	public static String getOracleDateFormat() {
		return "YYYY-MM-DD";
	}

	/**
	 * 获取Oracle的日期时间格式
	 * 
	 * @return String
	 */
	public static String getOracleDateTimeFormat() {
		return "YYYY-MM-DD HH24:MI:SS";
	}

	/**
	 * 获取一个从"1970-01-01 00:00:00"至现在的随机日期
	 * 
	 * @return RecDate
	 */
	public static RecDate getRandomRecDate() {
		return new RecDate((long) (System.currentTimeMillis() * Math.random()));
	}

	/**
	 * 根據所提供的格式化參數，格式化某個日期對象轉換為字符串並返回
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String formatDate(String pattern, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static void main(String[] args) {
		
		// System.out.println(RecDate.formatDate("yyyy/MM/dd", RecDate.getInstance().getDateObject()));
		// System.out.println(RecDate.checkDataIsValid("2012-05-05"));
		// System.out.println(RecDate.checkDataIsValid("2012-9-12"));
		// System.out.println(RecDate.checkDataIsValid("2012#8/8"));
		// System.out.println(RecDate.checkDataIsValid("2012-13-89"));
		// System.out.println(RecDate.getInstance("2012-20-89").getDateString());
		// System.out.println(RecDate.getInstance("2012-9-1").getFormatDateTimeString("yyyyMMdd"));
		// System.out.println(RecDate.getInstance().addDates(-7).getWeekFirstDate().getDateString());
		// System.out.println(RecDate.getInstance().getWeekLastDate().getDateString());
		// System.out.println(RecDate.getInstance().addDates(3).getDay());
		RecDate recDate = new RecDate("2013-01-31").setWeekFirstDate(RecDate.FRIDAY);
		System.out.println(recDate);
		System.out.println(recDate.getWeekFirstDate());
		System.out.println(recDate.getWeekLastDate());
	}

}
