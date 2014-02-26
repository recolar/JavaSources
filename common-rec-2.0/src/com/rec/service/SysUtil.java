package com.rec.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 系统工具类
 * 
 * @author Recolar
 * @version 1.0.0
 * @since JDK1.5.0
 */
public class SysUtil {

	private static final Log log = LogFactory.getLog(SysUtil.class);

	public static String zipAndEncodeByte(byte inByte[]) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		DeflaterOutputStream out = new DeflaterOutputStream(dest);
		try {
			out.write(inByte);
			out.finish();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] bytes = dest.toByteArray();
		String result = encoder.encode(bytes);
		return result;
	}

	public static String zipAndEncodeString(String inStr) {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		DeflaterOutputStream out = new DeflaterOutputStream(dest);
		try {
			out.write(inStr.getBytes("GBK"));
			out.finish();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] bytes = dest.toByteArray();
		String result = encoder.encode(bytes);
		return result;
	}

	public static String zipAndEncodeFile(String fileName) throws Exception {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		DeflaterOutputStream out = new DeflaterOutputStream(dest);
		FileInputStream in = new FileInputStream(fileName);
		byte[] buffer = new byte[1024];
		for (int count = -1; (count = in.read(buffer)) > 0;)
			out.write(buffer, 0, count);

		out.finish();
		BASE64Encoder encoder = new BASE64Encoder();
		in.close();
		return encoder.encode(dest.toByteArray());
	}

	public static String zipAndEncodeFile(File file) throws Exception {
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		DeflaterOutputStream out = new DeflaterOutputStream(dest);
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		for (int count = -1; (count = in.read(buffer)) > 0;)
			out.write(buffer, 0, count);

		out.finish();
		BASE64Encoder encoder = new BASE64Encoder();
		in.close();
		return encoder.encode(dest.toByteArray());
	}

	public static String encodeStream(InputStream in) throws Exception {
		BASE64Encoder encoder = new BASE64Encoder();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(in, out);
		return new String(out.toByteArray(), "GBK");
	}

	public static String encodeString(String inStr) {
		try {
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(inStr.getBytes("GBK"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String unzipAndDecodeString(String inStr) {
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			ByteArrayInputStream dest = new ByteArrayInputStream(decoder.decodeBuffer(inStr));
			InflaterInputStream in = new InflaterInputStream(dest);
			int count = 0;
			byte[] bytes = new byte[1024];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((count = in.read(bytes, 0, 1024)) != -1)
				out.write(bytes, 0, count);
			return new String(out.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final byte[] unzipAndDecodeStringToByte(String inStr) throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		ByteArrayInputStream dest = new ByteArrayInputStream(decoder.decodeBuffer(inStr));
		InflaterInputStream in = new InflaterInputStream(dest);
		int count = 0;
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((count = in.read(bytes, 0, 1024)) != -1)
			out.write(bytes, 0, count);
		return out.toByteArray();
	}

	public static String decodeString(String inStr) {
		try {
			if (inStr != null)
				inStr = inStr.replaceAll(" ", "\n");
			BASE64Decoder decoder = new BASE64Decoder();
			return new String(decoder.decodeBuffer(inStr));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSpace(int level) {
		String result = "";
		for (int i = 0; i < level; i++)
			result = result + " ";

		return result;
	}

	/**
	 * 根据properties的文件路径获取Properties对象
	 * 
	 * @param filePath properties文件路径
	 * @return Properties对象
	 */
	public static Properties readPropertiesFromFile(String filePath) {
		FileInputStream in = null;
		Properties prop = null;
		try {
			in = new FileInputStream(filePath);
			prop = new Properties();
			prop.load(in);// 根据InputStream构造Properties对象
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	/**
	 * Oracle驱动字符串
	 */
	public static final String ORG_DRIVER = "oracle.jdbc.driver.OracleDriver";

	/**
	 * Sql Server2000 驱动字符串
	 */
	public static final String SQL_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

	/**
	 * 根据ip地址，端口号和实例名创建Oracle数据库的连接URL
	 * 
	 * @param ip 数据库所在的IP地址
	 * @param port 数据库所在的端口号
	 * @param driverName 数据库实例名
	 * @return 连接url
	 */
	public static String getOraUrl(String ip, String port, String driverName) {
		StringBuffer url = new StringBuffer("jdbc:oracle:thin:@");
		return url.append(ip).append(":").append(port).append(":").append(driverName).toString();
	}

	/**
	 * 根据ip地址，端口号和实例名创建SqlServer数据库的连接URL
	 * 
	 * @param ip 数据库所在的IP地址
	 * @param port 数据库所在的端口号
	 * @param driverName 数据库实例名
	 * @return 连接url
	 */
	public static String getSqlServerUrl(String ip, String port, String driverName) {
		StringBuffer url = new StringBuffer("jdbc:microsoft:sqlserver://");
		return url.append(ip).append(":").append(port).append(";databaseName=").append(driverName).toString();
	}

	/**
	 * 判断一个字符串是否为数字
	 * 
	 * @param str 需要判断的字符串
	 * @return true:是数字 false:不是数字
	 */
	public static boolean isNum(String str) {
		boolean isNum = false;
		try {
			Double.parseDouble(str);
			isNum = true;
		} catch (Exception e) {
		}
		return isNum;
	}

	/**
	 * 获取物理硬盘中的某个文件的内容,以字符串的形式返回
	 * 
	 * @param filePath 文件路径
	 * @param isChangeLine 每读取一行之后是否要换行
	 * @return String 文件内容
	 */
	public static String readStringFromFile(String filePath, boolean isChangeLine) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			return readStringFromBufferedReader(br, isChangeLine);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String readStringFromBufferedReader(BufferedReader br, boolean isChangeLine) {
		StringBuffer content = new StringBuffer();
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				content.append(line);
				if (isChangeLine)
					content.append("\n");
			}
		} catch (FileNotFoundException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage(), e);
			else
				e.printStackTrace();
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage(), e);
			else
				e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
				}
		}
		return content.toString();
	}

	public static String readStringFromInputStream(InputStream is, boolean isChangeLine) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return readStringFromBufferedReader(br, isChangeLine);
	}

	/**
	 * 把一个JavaBean对象中各个属性的值拼凑成字符串形式返回
	 * 
	 * @param obj JavaBean对象
	 * @param isPrintDataType 是否把数据类型都连接起来
	 * @param delim 各个属性值之间的字符串分隔符
	 * @return 返回如"age:22 name=sss"等等的形式
	 */
	public static String objectToString(Object obj, boolean isPrintDataType, String delim) {
		StringBuffer objStr = new StringBuffer("");
		try {
			Class<? extends Object> classType = obj.getClass();
			Field[] fields = classType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();

				String getMethodName = "get" + StringUtil.initCap(fieldName);
				Method getMethod = classType.getMethod(getMethodName, new Class[] {});
				Object retObj = getMethod.invoke(obj, new Object[] {});
				if (isPrintDataType) {
					objStr.append(fieldName).append("(").append(field.getType().getName()).append("):").append(retObj)
							.append(delim);
				} else {
					objStr.append(fieldName).append(":").append(retObj).append(delim);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return objStr.toString();
	}

	/**
	 * 把一个JavaBean对象中各个属性的值拼凑成字符串形式返回
	 * 
	 * @param obj JavaBean对象
	 * @return 返回如"age:22 name=sss"等等的形式
	 */
	public static String objectToString(Object obj) {
		return objectToString(obj, false, "\t");

	}

	/**
	 * 把一个Object数组各个元素的值组成一个字符串，并返回
	 * 
	 * @param objs Object数组
	 * @param isPrintDataType 是否把数据类型都连接起来
	 * @param delim 各个元素值之间的字符串分隔符
	 * @return String
	 */
	public static String objectArrayToString(Object[] objs, boolean isPrintDataType, String delim) {
		StringBuffer str = new StringBuffer("");
		for (int i = 0; i < objs.length; i++) {
			Object object = objs[i];
			if (isPrintDataType) {
				str.append(object == null ? "" : "(" + object.getClass().getName() + "):");
				str.append(object);
			} else {
				str.append(object);
			}
			if (i < objs.length - 1) {
				str.append(delim);
			}
		}
		return str.toString();
	}

	/**
	 * 把一个Object数组各个元素的值组成一个字符串，并返回
	 * 
	 * @param objs Object数组
	 * @return String
	 */
	public static String objectArrayToString(Object[] objs) {
		return objectArrayToString(objs, false, "\t");
	}

	/**
	 * 把一个整型数组中的连续的各个数值，排序后把连续的数字保存在int[]数组中,然后int[]用List集合保存<br>
	 * 例如:数组对象为[1,2,3,6,7,9,34,56,57,88],则返回的List对象保存的各个元素如下:<br>
	 * [1,2,3],[6,7],[9],[34],[56,57,88]
	 * 
	 * @param nums int[]
	 * @return List
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<?> groupByConstantArray(int[] nums) {
		Arrays.sort(nums);
		int a = nums[0];
		List numList = new ArrayList();
		List tempList = new ArrayList();
		tempList.add(new Integer(a));
		for (int i = 1; i < nums.length; i++) {
			int b = nums[i];
			if (b == (1 + a)) {
				tempList.add(new Integer(b));
			} else {
				numList.add(listToIntArray(tempList));
				tempList.clear();
				tempList.add(new Integer(b));
			}
			if (i == (nums.length - 1)) {
				numList.add(listToIntArray(tempList));
			}
			a = b;
		}
		return numList;
	}

	/**
	 * 将字符串写入到文本
	 * 
	 * @param str 写入到文本的字符串
	 * @param filePath 写入文本路径,如:"C:/"
	 * @param fileName 写入文本名称,如:"aa.txt"
	 */
	public static void writeStringToText(String str, String filePath, String fileName) {
		String fullFilePath = "";
		char relex = filePath.charAt(filePath.length() - 1);
		if (relex == '\\') {
			fullFilePath = filePath + fileName;
		} else {
			fullFilePath = filePath + "/" + fileName;
		}
		writeStringToText(str, fullFilePath);
	}

	/**
	 * 将字符串写入到文本
	 * 
	 * @param str 写入到文本的字符串
	 * @param fullFilePath 文本的完整路径
	 */
	public static void writeStringToText(String str, String fullFilePath) {
		FileOutputStream f1 = null;
		OutputStreamWriter f2 = null;
		BufferedWriter writer = null;
		try {
			f1 = new FileOutputStream(fullFilePath);
			f2 = new OutputStreamWriter(f1);
			writer = new BufferedWriter(f2);
			writer.write(str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (f2 != null) {
					f2.close();
				}
				if (f1 != null) {
					f1.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存整数数的一个List集合对象，转换为一个整型数组
	 * 
	 * @param tempList List集合
	 * @return int[]
	 */
	@SuppressWarnings("rawtypes")
	public static int[] listToIntArray(List tempList) {
		int[] nums = new int[tempList.size()];
		int index = 0;
		for (Iterator i = tempList.iterator(); i.hasNext();) {
			nums[index++] = ((Integer) (i.next())).intValue();
		}
		return nums;
	}

	/**
	 * 集合对象深度比较<br>
	 * 如果两个集合对象长度相等,并且每个元素的对象相等,则返回true,否则返回false
	 * 
	 * @param c1 List对象
	 * @param c2 List对象
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean listDeepEquals(List c1, List c2) {
		if (c1 == null || c2 == null) {
			return false;
		}
		if (c1.size() != c2.size()) {
			return false;
		}
		for (int i = 0; i < c1.size(); i++) {
			Object obj1 = c1.get(i);
			Object obj2 = c2.get(i);
			if (!obj1.equals(obj2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 浪琩琌ら戳Α
	 * 
	 * @param date String "YYYY-MM-DD"ら戳Α才﹃
	 * @return true:琌
	 */
	public static boolean checkIsDate(String date) {
		if (date == null || date.trim().length() < 8) {
			return false;
		}
		date = date.trim();
		if (date.split("-").length != 3) {
			return false;
		}
		String[] dp = date.split("-");
		String year = dp[0];
		String month = dp[1];
		String day = dp[2];
		if (year.length() == 4 && month.length() < 3 && day.length() < 3) {
		}
		return false;
	}

}
