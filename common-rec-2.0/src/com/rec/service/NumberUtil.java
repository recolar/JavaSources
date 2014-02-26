package com.rec.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ��ѧ�����߼���
 * 
 * @author Recolar
 * @version 1.0.0 2006-11-03
 * @since JDK1.5.0
 */
public abstract class NumberUtil {

	private static final int DEF_DIV_SCALE = 10;

	private static final Random rand = new Random();

	public static final boolean randomBoolean = rand.nextBoolean();

	/**
	 * ȥ�������ظ�Ԫ�ص�����
	 * 
	 * @param nums ��������
	 * @return int[] ȥ���ظ�Ԫ�غ������
	 */
	public static int[] filterRepeatNum(int[] nums) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < nums.length; i++) {
			if (!list.contains(new Integer(nums[i]))) {
				list.add(new Integer(nums[i]));
			}
		}
		int[] filterNums = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			filterNums[i] = ((Integer) list.get(i)).intValue();
		}
		return filterNums;
	}

	/**
	 * �������������С����������<br>
	 * 
	 * @param nums ��������
	 * @return int[] ����������
	 */
	public static int[] getSortByIntArray(int[] nums) {
		Arrays.sort(nums);
		return nums;
	}

	/**
	 * �Ը������������С����������<br>
	 * 
	 * @param ��������
	 * @return ����������
	 */
	public static double[] getSortByIntArray(double[] nums) {
		Arrays.sort(nums);
		return nums;
	}

	/**
	 * ������������д�С������<br>
	 * 
	 * @param ��������
	 * @return ����������
	 */
	public static int[] getRelateSortByIntArray(int[] nums) {
		for (int i = 0, len = nums.length - 1; i < len; i++) {
			for (int j = 0; j < len - i; j++) {
				if (nums[j] < nums[j + 1]) {
					nums[j] = nums[j + 1] - nums[j];
					nums[j + 1] = nums[j + 1] - nums[j];
					nums[j] = nums[j] + nums[j + 1];
				}
			}
		}
		return nums;
	}

	/**
	 * �Ը�����������д�С������<br>
	 * 
	 * @param ��������
	 * @return ����������
	 */
	public static double[] getRelateSortByDoubleArray(double[] nums) {
		for (int i = 0, len = nums.length - 1; i < len; i++) {
			for (int j = 0; j < len - i; j++) {
				if (nums[j] < nums[j + 1]) {
					nums[j] = nums[j + 1] - nums[j];
					nums[j + 1] = nums[j + 1] - nums[j];
					nums[j] = nums[j] + nums[j + 1];
				}
			}
		}
		return nums;
	}

	/**
	 * �����ֽ��д����õ������ֵ����Ĵ�д��ʾ
	 * 
	 * @param num ��������
	 * @return String ���Ĵ�д��ʾ�ַ���
	 */
	public static String getChineseNumByArray(double num) {
		// Ҽ��������½��ƾ�ʰ��
		return null;
	}

	/**
	 * ��֤���ṩ�Ĳ����Ƿ�Ϊһ������<br>
	 * 
	 * @param num ����Ϊ������ʽ���ַ�������
	 */
	public static boolean isNumber(String num) {
		if (num.trim().length() == 0) {
			return false;
		} else if (num.trim().length() == 1) {
			return num.matches("\\d+");
		} else {
			return num.matches("\\d+[.]?\\d+");
		}
	}

	/**
	 * �ṩ��ȷ�ļӷ����㡣
	 * 
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������ĺ�
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * �ṩ��ȷ�ļ������㡣
	 * 
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������Ĳ�
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * �ṩ��ȷ�ĳ˷����㡣
	 * 
	 * @param v1 ������
	 * @param v2 ����
	 * @return ���������Ļ�
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ�� С�����Ժ�10λ���Ժ�������������롣
	 * 
	 * @param v1 ������
	 * @param v2 ����
	 * @return ������������
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ �����ȣ��Ժ�������������롣
	 * 
	 * @param v1 ������
	 * @param v2 ����
	 * @param scale ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
	 * @return ������������
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * �ṩ��ȷ��С��λ�������봦��
	 * 
	 * @param v ��Ҫ�������������
	 * @param scale С���������λ
	 * @return ���������Ľ��
	 */
	public static double round(double num, int scale) {
		// ����1��
		// double p = Math.pow(10, scale);
		// num *= p;
		// num = Math.round(num);
		// return num / p;

		// ����2
		BigDecimal b1 = new BigDecimal(num);
		BigDecimal b2 = new BigDecimal(1);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * ��ȡһ�������������
	 * 
	 * @param beginNum ������ֵ��±�
	 * @param endNum ������ֵ��ϱ�
	 * @return ������beginNum��endNum֮���һ���������
	 */
	public static int getRandomInt(int beginNum, int endNum) {
		return rand.nextInt(Math.abs(endNum - beginNum) + 1) + Math.min(beginNum, endNum);
	}

	/**
	 * ��һ�����ַ�Χ�ڣ���ȡĳ����������������������鷵��<br>
	 * ���beginNumΪ12,endNumΪ100,countΪ30,����˼Ϊ��12��100֮���ȡ30��û���ظ���������ֲ������鷵��<br>
	 * ���count��ֵС��beginNum��endNum֮���������ֵ����ô���ص�����Ϊint[0]
	 * 
	 * @param beginNum �ϱ�����
	 * @param endNum �±�����
	 * @param count ����
	 * @return int[]
	 */
	public static int[] getRandonIntArray(int beginNum, int endNum, int count) {
		if (count < 1) {
			return new int[0];
		}
		int minNum = Math.min(beginNum, endNum);
		int maxNum = Math.max(beginNum, endNum);
		int areaNum = maxNum - minNum + 1;
		if (count > areaNum) {
			return new int[0];
		}
		if (count == areaNum) {
			int[] nums = new int[count];
			int index = 0;
			for (int i = beginNum; i <= endNum; i++) {
				nums[index++] = i;
			}
			return nums;
		}
		List<Object> numsList = new ArrayList<Object>();
		if ((areaNum / 2) >= count) {
			while (true) {
				Integer num = new Integer(rand.nextInt(areaNum) + beginNum);
				if (!numsList.contains(num)) {
					numsList.add(num);
				}
				if (numsList.size() == count) {
					break;
				}
			}
		} else {
			for (int i = beginNum; i <= endNum; i++) {
				numsList.add(new Integer(i));
			}
			List<Object> tempList = new ArrayList<Object>();
			while (true) {
				Integer num = new Integer(rand.nextInt(areaNum) + beginNum);
				if (!tempList.contains(num)) {
					tempList.add(num);
				}
				if (tempList.size() == (areaNum - count)) {
					break;
				}
			}
			numsList.removeAll(tempList);
		}
		return SysUtil.listToIntArray(numsList);
	}

	/**
	 * ��������ת��ΪList���϶���<br>
	 * List�б�����Ƿ�װ��Integer
	 * 
	 * @param nums int[]
	 * @return List���϶���
	 */
	public static List<Object> intArrayToList(int[] nums) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < nums.length; i++) {
			list.add(new Integer(nums[i]));
		}
		return list;
	}

	/**
	 * ��ȡһ�������������
	 * 
	 * @return int
	 */
	public static int getRandomInt() {
		return rand.nextInt();
	}

	/**
	 * ��ȡһ�����������
	 * 
	 * @return double
	 */
	public static double getRandomDouble() {
		return rand.nextDouble();
	}

	/**
	 * ��ȡһ�������������
	 * 
	 * @param beginNum ������������±�
	 * @param endNum ������������ϱ�
	 * @return double
	 */
	public static double getRandomDouble(double beginNum, double endNum) {
		double minNum = Math.min(beginNum, endNum);
		double areaNum = Math.abs(beginNum - endNum);
		if (areaNum <= 1) {
			return minNum + areaNum * Math.random();
		}
		int randNum = (int) areaNum;
		return minNum + rand.nextInt(randNum + 1) + (areaNum - randNum) * Math.random();
	}

	private static final DecimalFormat SIGNAL1000_FORMATTER = new DecimalFormat("#,##0.00");

	/**
	 * ��ǧ��λ����ʽ��ʽ������
	 * 
	 * @param num double����
	 * @return ��ʽ������ַ���
	 */
	public static String formatNumberBy1000Signal(double num) {
		return SIGNAL1000_FORMATTER.format(num);
	}

	/**
	 * �ṩ��ʽ���ַ�������ʽ������
	 * 
	 * @param pattern ��ʽ���ַ���
	 * @param d ����
	 * @return
	 */
	public static String format(String pattern, double d) {
		return new DecimalFormat(pattern).format(d);
	}

	private static final DecimalFormat PERCENT_FORMATTER = new DecimalFormat("#0.##%");

	/**
	 * ��ĳһ��������ת���ɰٷ����ַ�����ʽ����
	 * 
	 * @param num ��Ҫת���ĸ�����
	 * @param scale ת���ٷ������������ľ���
	 * @return String �ٷ���
	 */
	public static String transNumberToPersentNumber(double num, int scale) {
		return transNumberToPersentNumber(new BigDecimal(num), scale);
	}

	/**
	 * ��ĳһ��������ת���ɰٷ����ַ�����ʽ����
	 * 
	 * @param num ��Ҫת����BigDecimal
	 * @param scale ת���ٷ������������ľ���
	 * @return String �ٷ���
	 */
	public static String transNumberToPersentNumber(BigDecimal num, int scale) {
		PERCENT_FORMATTER.setMaximumFractionDigits(scale);
		return PERCENT_FORMATTER.format(num);
	}

	/**
	 * ��ĳһ���ַ�������ʾ������ת���ɰٷ����ַ�����ʽ����
	 * 
	 * @param num ��Ҫת�����ַ�������
	 * @param scale ת���ٷ������������ľ���
	 * @return String �ٷ���
	 */
	public static String transNumberToPersentNumber(String num, int scale) {
		return transNumberToPersentNumber(new BigDecimal(Double.parseDouble(num)), scale);
	}

	/**
	 * ��ʽ������,����ĳ��С��λ
	 * 
	 * @param num ������
	 * @param scale С���������λ��
	 * @return double
	 */
	public static double formatNumber(double num, int scale) {
		return Double.parseDouble(String.format("%." + scale + "f", num));
	}

	public static void main(String[] args) {
		// System.out.println(isNumber("954.168"));
		// System.out.println(isNumber("954z"));
		// System.out.println(isNumber("954"));
		// System.out.println(isNumber("954Af"));
		// System.out.println(isNumber("0.43"));
		System.out.println(round(12.125, 2));
		System.out.println(round(12.124, 2));
		System.out.println(round(12.12545, 4));
		System.out.println(round(12.13, 1));
	}
}
