package com.rec.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.rec.exception.RecRunTimeException;

/**
 * ���ڲ�����
 * 
 * @author Recolar
 * @version 1.0.1 2006-12-30
 * @see
 * @since JDK1.5.0
 */
public final class RecDate implements java.io.Serializable, Cloneable, Comparable<Object> {

	/**
	 * ��������,�ڲ�ʼ��ά�ִ˶�����Ϊ������
	 */
	private Calendar calendar = Calendar.getInstance();

	/**
	 * ��ʽ������ʱ����ַ���
	 */
	private static final String DATETIME_STR_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * ��ʽ�����ڵ��ַ���
	 */

	private static final String DATE_STR_PATTERN = "yyyy-MM-dd";

	/**
	 * ���ڸ�ʽ������,��ʽ��:"2008-01-31" private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * /** ���ڸ�ʽ������,��ʽ��:"2008-01-31 12:12:05"
	 */
	private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(DATETIME_STR_PATTERN);// ����ʱ���ʽ������

	private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_STR_PATTERN);// ���ڸ�ʽ������

	private static final long serialVersionUID = 1L;

	/**
	 * ����ÿ�����ڵĵ�һ�������ڼ���һΪ1����Ϊ0��Ĭ��Ϊ������
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
	 * Ĭ�Ϲ��캯��<br>
	 */
	public RecDate() {
		super();
	}

	/**
	 * �����ڶ�����˶���
	 * 
	 * @param date util���µ����ڶ���
	 */
	public RecDate(Date date) {
		this.calendar.setTimeInMillis(date.getTime());
	}

	/**
	 * ����,��,�չ������ڶ���
	 * 
	 * @param year ���
	 * @param month �·�
	 * @param date ����
	 */
	public RecDate(int year, int month, int date) {
		this("" + year + "-" + month + "-" + date);
	}

	/**
	 * ����,��,��,ʱ,��,�빹�����ڶ���<br>
	 * 
	 * @param year ���
	 * @param month �·�
	 * @param date ����
	 * @param hours ʱ
	 * @param minutes ��
	 * @param seconds ��
	 */
	public RecDate(int year, int month, int date, int hours, int minutes, int seconds) {
		this.calendar.set(year, month - 1, date, hours, minutes, seconds);

	}

	/**
	 * ��java.sql.Date����RecDate����
	 * 
	 * @param date java.sql.Date
	 */
	public RecDate(java.sql.Date date) {
		this.calendar.setTimeInMillis(date.getTime());

	}

	/**
	 * ��long����(��1970��)�����ĺ���������RecDate����
	 * 
	 * @param times ������
	 */
	public RecDate(long times) {
		this.calendar.setTimeInMillis(times);

	}

	/**
	 * ���ַ�����ʼ������ֵ
	 * 
	 * @param dateTime ʱ�����ڵ��ַ���,��ʽ��:2006-12-12����2006-12-12 08:15:23
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
	 * ��java.sql.Timestamp����RecDate����
	 * 
	 * @param date java.sql.Timestamp
	 */
	public RecDate(Timestamp date) {
		this.calendar.setTimeInMillis(date.getTime());
	}

	/**
	 * �ڵ�ǰ�����յĻ����ϼ���һ������
	 * 
	 * @param addNum ����,��Ϊ��������ȥһ����ֵ
	 */
	public RecDate addDates(int addNum) {
		this.calendar.add(Calendar.DATE, addNum);
		return this;
	}

	/**
	 * �ڵ�ǰ���ڵ�Сʱ�Ļ����ϼ���һ��Сʱ������
	 * 
	 * @param addNum ����,��Ϊ��������ȥһ��Сʱ��ֵ
	 */
	public RecDate addHours(int addNum) {
		this.calendar.add(Calendar.HOUR, addNum);
		return this;
	}

	/**
	 * �ڵ�ǰ���ڵķ��ӵĻ����ϼ���һ�����ӵ�����
	 * 
	 * @param addNum ����,��Ϊ��������ȥһ��������ֵ
	 */
	public RecDate addMinutes(int addNum) {
		this.calendar.add(Calendar.MINUTE, addNum);
		return this;
	}

	/**
	 * �ڵ�ǰ���ڵ��·ݵĻ����ϼ���һ���·ݵ�����
	 * 
	 * @param addNum ����,��Ϊ��������ȥһ���·���ֵ
	 */
	public RecDate addMonths(int addNum) {
		this.calendar.add(Calendar.MONDAY, addNum);
		return this;
	}

	/**
	 * �ڵ�ǰ���ڵ������Ļ����ϼ���һ������������
	 * 
	 * @param addNum ����,��Ϊ��������ȥһ��������ֵ
	 */
	public RecDate addSeconds(int addNum) {
		this.calendar.add(Calendar.SECOND, addNum);
		return this;
	}

	/**
	 * �ڵ�ǰ���ڵ���ݵĻ����ϼ���һ����ݵ�����
	 * 
	 * @param addNum ����,��Ϊ��������ȥһ�������ֵ
	 */
	public RecDate addYears(int addNum) {
		this.calendar.add(Calendar.YEAR, addNum);
		return this;
	}

	/**
	 * ��¡һ����ǰʵ��,���
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
			throw new RecRunTimeException("���ṩ��������������ض���");
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
	 * �������date���ڱ�����ʱ��ֵ����1����<br>
	 * ���С��date���ڱ�����ʱ��ֵ����-1����<br>
	 * �������date���ڱ�����ʱ��ֵ����0����<br>
	 * 
	 * @param date ��Ҫ����Ƚϵ�Date���ڱ���
	 * @return RecDate������Date���������ȽϽ��ֵ
	 */
	public int compareToDate(Date date) {
		return this.calendar.getTime().getTime() == date.getTime() ? 0 : (this.calendar.getTime().getTime() > date
				.getTime() ? 1 : -1);
	}

	/**
	 * ���ַ���ʱ���ʱ��ֵ�Ա�,������ڷ���1���ڷ���0С�ڷ���-1<br>
	 * 
	 * @param date String���ڱ���
	 * @return �Ա�ֵ
	 */
	public int compareToDate(String date) {
		if (!RecDate.checkDataIsValid(date)) {
			throw new RecRunTimeException("���ṩ�����ڸ�ʽ����ȷ��ӦΪyyyy-mm-dd����yyyy-MM-dd HH:mm:ss�ĸ�ʽ��ʽ");
		}
		RecDate rd = new RecDate(date);// ����ֵ
		return getTime() == rd.getTime() ? 0 : (getTime() > rd.getTime() ? 1 : -1);
	}

	/**
	 * ��ȡ��ǰ����<br>
	 * 
	 * @return ��ǰ����
	 */
	public int getDate() {
		return this.calendar.get(Calendar.DATE);
	}

	/**
	 * ��ȡ���������֮�����������
	 * 
	 * @param date Date����
	 * @return ���������(�����Ծ���ֵ)
	 */
	public long getDateInteval(Date date) {
		return Math.abs((long) (getTime() - date.getTime()) / (86400 * 1000));
	}

	/**
	 * ��ȡ���������֮�����������
	 * 
	 * @param date RecDate����
	 * @return ���������(�����Ծ���ֵ)
	 */
	public long getDateInteval(RecDate date) {
		return getDateInteval(date.getDateObject());
	}

	/**
	 * ��ȡ������ַ�������֮�����������
	 * 
	 * @param date �ַ�����������ʾ��String����
	 * @return ���������
	 */
	public long getDateInteval(String date) {
		if (!RecDate.checkDataIsValid(date)) {
			throw new RecRunTimeException("�������ڲ��Ϸ�,�����ַ������ڲ�����ʽ�Ƿ���ȷ");
		}
		return getDateInteval(RecDate.getInstance(date).getDateObject());
	}

	/**
	 * �������ڶ�����������<br>
	 * ����1��ģ���0����
	 * 
	 * @param date
	 * @return
	 */
	public int getYearInteval(RecDate date) {
		return (int) Math.abs(getDateInteval(date) / 365);
	}

	/**
	 * ����һ��java.util.Date����<br>
	 * 
	 * @return Date����
	 */
	public Date getDateObject() {
		return (Date) this.calendar.getTime().clone();
	}

	/**
	 * �����ַ�������<br>
	 * 
	 * @return �ַ�������
	 */
	public String getDateString() {
		return this.dateFormatter.format(this.calendar.getTime());
	}

	/**
	 * �������ṩ�ĸ�ʽ�����ַ�������ʽһ��ʱ��
	 * 
	 * @param format ��ʽ�����ڵ��ַ���
	 * @return String ��ʽ���ַ���
	 */
	public String getFormatDateTimeString(String format) {
		return new SimpleDateFormat(format).format(this.calendar.getTime());
	}

	/**
	 * �����ַ�������ʱ��<br>
	 * 
	 * @return �ַ�������ʱ��
	 */
	public String getDateTimeString() {
		return this.dateTimeFormatter.format(this.calendar.getTime());
	}

	/**
	 * ��ȡ��ǰ����Ϊ���ڼ�,1Ϊ����һ,0Ϊ������
	 * 
	 * @return ���ڼ���ֵ
	 */
	public int getDay() {
		return this.calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * ��ȡСʱ<br>
	 * 
	 * @return Сʱ
	 */
	public int getHours() {
		return this.calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * ��ȡ���������ո�ʽ
	 * 
	 * @return String
	 */
	public String getChinaDateString() {
		StringBuffer str = new StringBuffer();
		str.append(getYear()).append("��");
		str.append(getMonth()).append("��");
		str.append(getDate()).append("��");
		return str.toString();
	}

	public String getChinaDateTimeString() {
		StringBuffer str = new StringBuffer();
		str.append(getYear()).append("��");
		str.append(getMonth()).append("��");
		str.append(getDate()).append("��");
		str.append(" ").append(getHours()).append("ʱ");
		str.append(getMinutes()).append("��");
		str.append(getSeconds()).append("��");
		return str.toString();
	}

	public String getDayStr() {
		String dayStr = "";
		int day = getDay();
		if (day == 1) {
			dayStr = "һ";
		} else if (day == 2) {
			dayStr = "��";
		} else if (day == 3) {
			dayStr = "��";
		} else if (day == 4) {
			dayStr = "��";
		} else if (day == 5) {
			dayStr = "��";
		} else if (day == 6) {
			dayStr = "��";
		} else if (day == 0) {
			dayStr = "��";
		}
		return dayStr;
	}

	/**
	 * �������ڼ�
	 * 
	 * @return String
	 */
	public String getLocalWeekDayString() {
		return "����" + getDayStr();
	}

	/**
	 * ���ظ�ʽ�磺2012��3��4�ա���������
	 * 
	 * @return
	 */
	public String getLocalDateAndWeekDateString() {
		return getChinaDateString() + "��" + getLocalWeekDayString() + "��";
	}

	/**
	 * ��ȡ��ǰ�����ڵ�ǰ��ĵڼ���
	 * 
	 * @return int
	 */
	public int getDateOfYear() {
		return this.calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * ��ȡ��ǰ�����ǵ�ǰ�·����ڵĵڼ�������
	 * 
	 * @return int
	 */
	public int getWeekOfMonth() {
		return this.calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * ��ȡ��ǰ�����ǵ�ǰ������ڵĵڼ�������
	 * 
	 * @return int
	 */
	public int getWeekOfYear() {
		return this.calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * ��ȡ����<br>
	 * 
	 * @return ����
	 */
	public int getMinutes() {
		return this.calendar.get(Calendar.MINUTE);
	}

	/**
	 * ��ȡ�·�<br>
	 * 
	 * @return �·�
	 */
	public int getMonth() {
		return this.calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * ��õ��µĵ�һ��<br>
	 * 
	 * @return ��ǰ�·ݵĵ�һ��
	 */
	public String getMonthFirstDate() {
		int month = getMonth();
		return getYear() + "-" + (month < 10 ? ("0" + month) : String.valueOf(month)) + "-01";
	}

	/**
	 * ��õ�ǰ�µ����һ��<br>
	 * 
	 * @return ��ǰ�·ݵ����һ��
	 */
	public String getMonthLastDate() {
		Calendar cld = (Calendar) this.calendar.clone();
		cld.set(getYear(), getMonth(), 1);
		cld.add(Calendar.DATE, -1);
		return this.dateTimeFormatter.format(cld.getTime());
	}

	/**
	 * ����ÿ�����ڵĵ�һ�������ڼ�,����һΪ1��������Ϊ0��Ĭ��Ϊ������<br>
	 * ����ͨ��RecDate.MONDAY�ȵȷ�ʽ��ֵ
	 * 
	 * @param day
	 */
	public RecDate setWeekFirstDate(int day) {
		this.weekFirstDay = Math.abs(day) % 7;
		return this;
	}

	/**
	 * ��ȡ��ǰ�ܵĵ�һ��
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
	 * ��ȡ��ǰ�ܵ����һ��
	 * 
	 * @return
	 */
	public RecDate getWeekLastDate() {
		return getWeekFirstDate().addDates(6);
	}

	/**
	 * ��ȡ����<br>
	 * 
	 * @return ����
	 */
	public int getSeconds() {
		return this.calendar.get(Calendar.SECOND);
	}

	/**
	 * ����java.sql.Date����<br>
	 * 
	 * @return java.sql.Date����
	 */
	public java.sql.Date getSqlDateObject() {
		return new java.sql.Date(this.calendar.getTimeInMillis());
	}

	/**
	 * ��ȡ1970-01-01��ʱ�������������ĺ�����<br>
	 * 
	 * @return ������Ŀ
	 */
	public long getTime() {
		return this.calendar.getTimeInMillis();
	}

	/**
	 * ����java.sql.Timestamp����<br>
	 * 
	 * @return java.sql.Timestamp����
	 */
	public Timestamp getTimestampObject() {
		return new Timestamp(this.calendar.getTimeInMillis());
	}

	/**
	 * ��ȡ���<br>
	 * 
	 * @return ���
	 */
	public int getYear() {
		return this.calendar.get(Calendar.YEAR);
	}

	/**
	 * �����ǰ�������ڲ����е������򷵻�true���򷵻�false
	 * 
	 * @param tempDate �Ƚϵ����ڲ���
	 * @return boolean
	 */
	public boolean isAfter(Date tempDate) {
		return getTime() > tempDate.getTime();
	}

	/**
	 * �����ǰ�������ڲ����еĺ�����˵���ɵ�����ֵ�Ļ��򷵻�true���򷵻�false
	 * 
	 * @param timesmillis ��1970-1-1��ʱ�������ĺ�����
	 * @return boolean
	 */
	public boolean isAfter(long timesmillis) {
		return getTime() > timesmillis;
	}

	/**
	 * �����ǰ�������ڲ����е������򷵻�true���򷵻�false
	 * 
	 * @param tempDate �Ƚϵ����ڲ���
	 * @return boolean
	 */
	public boolean isAfter(RecDate tempDate) {
		return getTime() > tempDate.getTime();
	}

	/**
	 * �����ǰ�������ڲ����е������򷵻�true���򷵻�false
	 * 
	 * @param tempDate �Ƚϵ����ڲ���
	 * @return boolean
	 */
	public boolean isBefore(Date tempDate) {
		return getTime() < tempDate.getTime();
	}

	/**
	 * �����ǰ�������ڲ����еĺ�����˵���ɵ�����ֵ�Ļ��򷵻�true���򷵻�false
	 * 
	 * @param timesmillis ��1970-1-1��ʱ�������ĺ�����
	 * @return boolean
	 */
	public boolean isBefore(long timesmillis) {
		return getTime() < timesmillis;
	}

	/**
	 * �����ǰ�������ڲ����е������򷵻�true���򷵻�false
	 * 
	 * @param tempDate �Ƚϵ����ڲ���
	 * @return boolean
	 */
	public boolean isBefore(RecDate tempDate) {
		return getTime() < tempDate.getTime();
	}

	/**
	 * ��������ڶ����������ո�ʽ��ʱ�򷵻�true���򷵻�false
	 * 
	 * @return boolean
	 */
	public boolean isDateType() {
		return (getHours() == 0 && getMinutes() == 0 && getSeconds() == 0);
	}

	/**
	 * �����ڶ�����������
	 * 
	 * @param date util�������ڶ���
	 */
	public void setDate(Date date) {
		this.calendar.setTimeInMillis(date.getTime());

	}

	/**
	 * ��������,������ڴ���31����С��0�������Ч
	 * 
	 * @param date ����
	 */
	public RecDate setDate(int date) {
		this.calendar.set(Calendar.DATE, date);
		return this;
	}

	/**
	 * �����ڶ�����������
	 * 
	 * @param date sql�������ڶ���
	 */
	public RecDate setDate(java.sql.Date date) {
		this.calendar.setTimeInMillis(date.getTime());
		return this;
	}

	/**
	 * �����ڶ�����������
	 * 
	 * @param date sql����Timestamp����
	 */
	public RecDate setDate(java.sql.Timestamp date) {
		this.calendar.setTimeInMillis(date.getTime());
		return this;
	}

	/**
	 * ���������ղ���
	 * 
	 * @param year ���
	 * @param month �·�
	 * @param date ����
	 */
	public RecDate setDateParam(int year, int month, int date) {
		this.calendar.set(year, month - 1, date, 0, 0, 0);
		return this;
	}

	/**
	 * ����������ʱ����Ȳ���
	 * 
	 * @param year ���
	 * @param month �·�
	 * @param date ����
	 * @param hours Сʱ
	 * @param minutes ����
	 * @param seconds ����
	 */
	public RecDate setDateTimeParam(int year, int month, int date, int hours, int minutes, int seconds) {
		this.calendar.set(year, month - 1, date, hours, minutes, seconds);
		return this;
	}

	/**
	 * ��ʱ�������ַ�����������
	 * 
	 * @param dateTime ʱ�����ڵ��ַ���,��ʽ��:2006-12-12����2006-12-12 08:15:23
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
	 * ����Сʱ,���Сʱֵ����23����С��0�������Ч
	 * 
	 * @param hour Сʱ
	 */
	public RecDate setHour(int hour) {
		if (hour < 0 || hour > 23) {
			throw new RecRunTimeException("Сʱ��ֻ��Ϊ0��23֮�������");
		}
		// this.calendar.set(getYear(), getMonth() - 1, getDate(), hour, getMinutes(), getSeconds());
		this.calendar.set(Calendar.HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * ���÷��ӣ��������С��0���ߴ���59�������Ч
	 * 
	 * @param minute ����
	 */
	public RecDate setMinute(int minute) {
		if (minute < 0 || minute > 59) {
			throw new RecRunTimeException("������ֻ����1��59֮�����ֵ");
		}
		// this.calendar.set(getYear(), getMonth() - 1, getDate(), getHours(), minute, getSeconds());
		this.calendar.set(Calendar.MINUTE, minute);
		return this;
	}

	/**
	 * �����·�,����·ݴ���12����С��1�������Ч
	 * 
	 * @param month �·�
	 */
	public RecDate setMonth(int month) {
		// this.calendar.set(getYear(), month - 1, getDate(), getHours(), getMinutes(), getSeconds());
		this.calendar.set(Calendar.MONDAY, month - 1);
		return this;
	}

	/**
	 * ���÷��ӣ��������С��0���ߴ���59�������Ч
	 * 
	 * @param second ����
	 */
	public RecDate setSecond(int second) {
		if (second < 0 || second > 59) {
			throw new RecRunTimeException("����ֻ����1��59֮�����ֵ");
		}
		// this.calendar.set(getYear(), getMonth() - 1, getDate(), getHours(), getMinutes(), second);
		this.calendar.set(Calendar.SECOND, second);
		return this;
	}

	/**
	 * ��long����(��1970��)�����ĺ���������RecDate����
	 * 
	 * @param times ������
	 */
	public RecDate setTime(long times) {
		this.calendar.setTimeInMillis(times);
		return this;
	}

	/**
	 * �������
	 * 
	 * @param year ���
	 */
	public RecDate setYear(int year) {
		// this.calendar.set(year, getMonth() - 1, getDate(), getHours(), getMinutes(), getSeconds());
		this.calendar.set(Calendar.YEAR, year);
		return this;
	}

	/**
	 * toString����<br>
	 * 
	 * @return ��ǰ����ʱ��
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
	 * ������ڸ�ʽ�Ƿ���ȷ
	 * 
	 * @return true:��ȷ false:ʧ��
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
		// // throw new RecRunTimeException("���ṩ�����ڸ�ʽ����ȷ��ӦΪyyyy-mm-dd����yyyy-MM-dd HH:mm:ss�ĸ�ʽ��ʽ", e1);
		// }
		// }
		// return isOk;
		return dateStr.matches("\\d{4}\\D\\d{1,2}\\D\\d{1,2}");
	}

	/**
	 * ���date1>date2�򷵻�1����<br>
	 * ���date1<date2�򷵻�-1����<br>
	 * ���date1=date2�򷵻ص���0����<br>
	 * 
	 * @param date1 ��һ��Date���ڱ���
	 * @param date2 �ڶ���Date���ڱ���
	 * @return ��һ������(date1)�͵ڶ�������(date2)�ıȽϽ��ֵ
	 */
	public static int dateCompareToDate(Date date1, Date date2) {
		return date1.getTime() == date2.getTime() ? 0 : (date1.getTime() > date2.getTime() ? 1 : -1);
	}

	/**
	 * ���date1>date2�򷵻�1����<br>
	 * ���date1<date2�򷵻�-1����<br>
	 * ���date1=date2�򷵻ص���0����<br>
	 * 
	 * @param date1 ��һ��Date���ڱ���
	 * @param date2 �ڶ���Date���ڱ���
	 * @return ��һ������(date1)�͵ڶ�������(date2)�ıȽϽ��ֵ
	 */
	public static int dateCompareToDate(String date1, String date2) {
		if (!RecDate.checkDataIsValid(date1)) {
			throw new RecRunTimeException("�������ڲ��Ϸ�,�����ַ������ڲ�����ʽ�Ƿ���ȷ");
		}
		if (!RecDate.checkDataIsValid(date2)) {
			throw new RecRunTimeException("�������ڲ��Ϸ�,�����ַ������ڲ�����ʽ�Ƿ���ȷ");
		}
		RecDate rd1 = new RecDate(date1);
		RecDate rd2 = new RecDate(date2);
		return rd1.getTime() == rd2.getTime() ? 0 : (rd1.getTime() > rd2.getTime() ? 1 : -1);
	}

	/**
	 * ��ȡ��ǰʱ��������ո�ʽ�ַ���
	 * 
	 * @return String
	 */
	public static String getCurrentDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new java.util.Date());
	}

	/**
	 * ��ȡ��ǰʱ���������ʱ������ַ�����ʽ
	 * 
	 * @return String
	 */
	public static String getCurrentDateTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(new java.util.Date());
	}

	/**
	 * ��ȡ��������֮�����������
	 * 
	 * @param date1 Date���ڶ���
	 * @param date2 Date���ڶ���
	 * @return int �����������������(�����Ծ���ֵ)
	 */
	public static int getDateInteval(Date date1, Date date2) {
		return Math.abs((int) ((date2.getTime() - date1.getTime()) / (86400 * 1000)));
	}

	/**
	 * ��ȡ��������֮�����������
	 * 
	 * @param date1 Date���ڶ���
	 * @param date2 Date���ڶ���
	 * @return int �����������������(�����Ծ���ֵ),�����ĳ��������ʱ�����ڲ��ǺϷ����򷵻�-1
	 */
	public static int getDateInteval(String date1, String date2) {
		if (!RecDate.checkDataIsValid(date1) || !RecDate.checkDataIsValid(date2)) {
			throw new RecRunTimeException("�������ڲ��Ϸ�,�����ַ������ڲ�����ʽ�Ƿ���ȷ");
		}
		return Math.abs((int) ((new RecDate(date1).getTime() - new RecDate(date2).getTime()) / (86400 * 1000)));
	}

	/**
	 * �����ַ������ڷ��ع���֮����ַ���<br>
	 * ��2006-12-12 00:00:00.0 ת����2006-12-12<br>
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
	 * ��ȡRecDate��һ��ʵ��,ʱ����ȡ��ǰʱ��
	 * 
	 * @return RecDateʵ��
	 */
	public static RecDate getInstance() {
		return new RecDate();
	}

	/**
	 * ���ݲ�����ȡһ��RecDateʵ��<br>
	 * �ò�������ʹString,java.util.Date,java.sql.Date,java.sql.Timestamp,Calendar
	 * 
	 * @param dateTimeStr ʱ�����
	 * @return RecDateʵ��
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
	 * ��ȡOracle�����ڸ�ʽ
	 * 
	 * @return String
	 */
	public static String getOracleDateFormat() {
		return "YYYY-MM-DD";
	}

	/**
	 * ��ȡOracle������ʱ���ʽ
	 * 
	 * @return String
	 */
	public static String getOracleDateTimeFormat() {
		return "YYYY-MM-DD HH24:MI:SS";
	}

	/**
	 * ��ȡһ����"1970-01-01 00:00:00"�����ڵ��������
	 * 
	 * @return RecDate
	 */
	public static RecDate getRandomRecDate() {
		return new RecDate((long) (System.currentTimeMillis() * Math.random()));
	}

	/**
	 * �������ṩ�ĸ�ʽ����������ʽ��ĳ�����ڌ����D�Q���ַ����K����
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
