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
 * ϵͳ������
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
	 * ����properties���ļ�·����ȡProperties����
	 * 
	 * @param filePath properties�ļ�·��
	 * @return Properties����
	 */
	public static Properties readPropertiesFromFile(String filePath) {
		FileInputStream in = null;
		Properties prop = null;
		try {
			in = new FileInputStream(filePath);
			prop = new Properties();
			prop.load(in);// ����InputStream����Properties����
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
	 * Oracle�����ַ���
	 */
	public static final String ORG_DRIVER = "oracle.jdbc.driver.OracleDriver";

	/**
	 * Sql Server2000 �����ַ���
	 */
	public static final String SQL_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

	/**
	 * ����ip��ַ���˿ںź�ʵ��������Oracle���ݿ������URL
	 * 
	 * @param ip ���ݿ����ڵ�IP��ַ
	 * @param port ���ݿ����ڵĶ˿ں�
	 * @param driverName ���ݿ�ʵ����
	 * @return ����url
	 */
	public static String getOraUrl(String ip, String port, String driverName) {
		StringBuffer url = new StringBuffer("jdbc:oracle:thin:@");
		return url.append(ip).append(":").append(port).append(":").append(driverName).toString();
	}

	/**
	 * ����ip��ַ���˿ںź�ʵ��������SqlServer���ݿ������URL
	 * 
	 * @param ip ���ݿ����ڵ�IP��ַ
	 * @param port ���ݿ����ڵĶ˿ں�
	 * @param driverName ���ݿ�ʵ����
	 * @return ����url
	 */
	public static String getSqlServerUrl(String ip, String port, String driverName) {
		StringBuffer url = new StringBuffer("jdbc:microsoft:sqlserver://");
		return url.append(ip).append(":").append(port).append(";databaseName=").append(driverName).toString();
	}

	/**
	 * �ж�һ���ַ����Ƿ�Ϊ����
	 * 
	 * @param str ��Ҫ�жϵ��ַ���
	 * @return true:������ false:��������
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
	 * ��ȡ����Ӳ���е�ĳ���ļ�������,���ַ�������ʽ����
	 * 
	 * @param filePath �ļ�·��
	 * @param isChangeLine ÿ��ȡһ��֮���Ƿ�Ҫ����
	 * @return String �ļ�����
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
	 * ��һ��JavaBean�����и������Ե�ֵƴ�ճ��ַ�����ʽ����
	 * 
	 * @param obj JavaBean����
	 * @param isPrintDataType �Ƿ���������Ͷ���������
	 * @param delim ��������ֵ֮����ַ����ָ���
	 * @return ������"age:22 name=sss"�ȵȵ���ʽ
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
	 * ��һ��JavaBean�����и������Ե�ֵƴ�ճ��ַ�����ʽ����
	 * 
	 * @param obj JavaBean����
	 * @return ������"age:22 name=sss"�ȵȵ���ʽ
	 */
	public static String objectToString(Object obj) {
		return objectToString(obj, false, "\t");

	}

	/**
	 * ��һ��Object�������Ԫ�ص�ֵ���һ���ַ�����������
	 * 
	 * @param objs Object����
	 * @param isPrintDataType �Ƿ���������Ͷ���������
	 * @param delim ����Ԫ��ֵ֮����ַ����ָ���
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
	 * ��һ��Object�������Ԫ�ص�ֵ���һ���ַ�����������
	 * 
	 * @param objs Object����
	 * @return String
	 */
	public static String objectArrayToString(Object[] objs) {
		return objectArrayToString(objs, false, "\t");
	}

	/**
	 * ��һ�����������е������ĸ�����ֵ�����������������ֱ�����int[]������,Ȼ��int[]��List���ϱ���<br>
	 * ����:�������Ϊ[1,2,3,6,7,9,34,56,57,88],�򷵻ص�List���󱣴�ĸ���Ԫ������:<br>
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
	 * ���ַ���д�뵽�ı�
	 * 
	 * @param str д�뵽�ı����ַ���
	 * @param filePath д���ı�·��,��:"C:/"
	 * @param fileName д���ı�����,��:"aa.txt"
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
	 * ���ַ���д�뵽�ı�
	 * 
	 * @param str д�뵽�ı����ַ���
	 * @param fullFilePath �ı�������·��
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
	 * ������������һ��List���϶���ת��Ϊһ����������
	 * 
	 * @param tempList List����
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
	 * ���϶�����ȱȽ�<br>
	 * ����������϶��󳤶����,����ÿ��Ԫ�صĶ������,�򷵻�true,���򷵻�false
	 * 
	 * @param c1 List����
	 * @param c2 List����
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
	 * �ˬd�O�_������榡
	 * 
	 * @param date String �Φp"YYYY-MM-DD"������榡�r�Ŧ�
	 * @return true:�O
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
