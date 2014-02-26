package com.rec.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 数学处理逻辑类
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
	 * 去掉具有重复元素的数组
	 * 
	 * @param nums 整型数组
	 * @return int[] 去掉重复元素后的数组
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
	 * 对整型数组进行小到大排序处理<br>
	 * 
	 * @param nums 整型数组
	 * @return int[] 排序后的数组
	 */
	public static int[] getSortByIntArray(int[] nums) {
		Arrays.sort(nums);
		return nums;
	}

	/**
	 * 对浮点数数组进行小到大排序处理<br>
	 * 
	 * @param 整型数组
	 * @return 排序后的数组
	 */
	public static double[] getSortByIntArray(double[] nums) {
		Arrays.sort(nums);
		return nums;
	}

	/**
	 * 对整型数组进行大到小排序处理<br>
	 * 
	 * @param 整型数组
	 * @return 排序后的数组
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
	 * 对浮点数数组进行大到小排序处理<br>
	 * 
	 * @param 整型数组
	 * @return 排序后的数组
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
	 * 对数字进行处理，得到该数字的中文大写表示
	 * 
	 * @param num 浮点数字
	 * @return String 中文大写表示字符串
	 */
	public static String getChineseNumByArray(double num) {
		// 壹贰叁肆伍陆柒捌玖拾零
		return null;
	}

	/**
	 * 验证所提供的参数是否为一个数字<br>
	 * 
	 * @param num 可以为数字形式的字符串参数
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
	 * 提供精确的加法运算。
	 * 
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
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
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double num, int scale) {
		// 方法1：
		// double p = Math.pow(10, scale);
		// num *= p;
		// num = Math.round(num);
		// return num / p;

		// 方法2
		BigDecimal b1 = new BigDecimal(num);
		BigDecimal b2 = new BigDecimal(1);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 获取一个随机整型数字
	 * 
	 * @param beginNum 随机数字的下标
	 * @param endNum 随机数字的上标
	 * @return 包括从beginNum到endNum之间的一个随机数字
	 */
	public static int getRandomInt(int beginNum, int endNum) {
		return rand.nextInt(Math.abs(endNum - beginNum) + 1) + Math.min(beginNum, endNum);
	}

	/**
	 * 在一个数字范围内，获取某个数量的随机数，并以数组返回<br>
	 * 如果beginNum为12,endNum为100,count为30,则意思为从12到100之间获取30个没有重复的随机数字并以数组返回<br>
	 * 如果count的值小于beginNum和endNum之间的区间数值，那么返回的数组为int[0]
	 * 
	 * @param beginNum 上标数字
	 * @param endNum 下标数字
	 * @param count 数量
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
	 * 整型数组转换为List集合对象<br>
	 * List中保存的是封装类Integer
	 * 
	 * @param nums int[]
	 * @return List集合对象
	 */
	public static List<Object> intArrayToList(int[] nums) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < nums.length; i++) {
			list.add(new Integer(nums[i]));
		}
		return list;
	}

	/**
	 * 获取一个随机整型数字
	 * 
	 * @return int
	 */
	public static int getRandomInt() {
		return rand.nextInt();
	}

	/**
	 * 获取一个随机浮点数
	 * 
	 * @return double
	 */
	public static double getRandomDouble() {
		return rand.nextDouble();
	}

	/**
	 * 获取一个随机浮点数字
	 * 
	 * @param beginNum 随机浮点数的下标
	 * @param endNum 随机浮点数的上标
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
	 * 用千分位的形式格式化数字
	 * 
	 * @param num double数字
	 * @return 格式化后的字符串
	 */
	public static String formatNumberBy1000Signal(double num) {
		return SIGNAL1000_FORMATTER.format(num);
	}

	/**
	 * 提供格式化字符串，格式化数字
	 * 
	 * @param pattern 格式化字符串
	 * @param d 数字
	 * @return
	 */
	public static String format(String pattern, double d) {
		return new DecimalFormat(pattern).format(d);
	}

	private static final DecimalFormat PERCENT_FORMATTER = new DecimalFormat("#0.##%");

	/**
	 * 将某一个浮点数转换成百分数字符串形式返回
	 * 
	 * @param num 需要转换的浮点数
	 * @param scale 转换百分数后所保留的精度
	 * @return String 百分数
	 */
	public static String transNumberToPersentNumber(double num, int scale) {
		return transNumberToPersentNumber(new BigDecimal(num), scale);
	}

	/**
	 * 将某一个大数字转换成百分数字符串形式返回
	 * 
	 * @param num 需要转换的BigDecimal
	 * @param scale 转换百分数后所保留的精度
	 * @return String 百分数
	 */
	public static String transNumberToPersentNumber(BigDecimal num, int scale) {
		PERCENT_FORMATTER.setMaximumFractionDigits(scale);
		return PERCENT_FORMATTER.format(num);
	}

	/**
	 * 将某一个字符串所表示的数字转换成百分数字符串形式返回
	 * 
	 * @param num 需要转换的字符串数字
	 * @param scale 转换百分数后所保留的精度
	 * @return String 百分数
	 */
	public static String transNumberToPersentNumber(String num, int scale) {
		return transNumberToPersentNumber(new BigDecimal(Double.parseDouble(num)), scale);
	}

	/**
	 * 格式化数字,保留某个小数位
	 * 
	 * @param num 浮点数
	 * @param scale 小数点后保留的位数
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
