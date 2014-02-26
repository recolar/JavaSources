package com.rec.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rec.exception.RecRunTimeException;

/**
 * 想控制台打印字符串的类
 * 
 * @author Recolar
 */
public class Console {

	private static BufferedReader in;

	/**
	 * 在控制台上换行打印对象<br>
	 * 
	 * @param obj 要打印的对象(toString方法打印出字符串)
	 */
	public static void writeLine(Object obj) {
		System.out.println(obj);
	}

	/**
	 * 在控制台上换行打印对象
	 * 
	 * @param obj 要打印的对象(toString方法打印出字符串)
	 * @param length 打印的行的宽度
	 */
	public static void writeLine(Object obj, int length) {
		length = (length < 1 ? Math.abs(length) : length);
		if (obj.toString().length() >= length) {
			writeLine(obj);
		} else {
			writeLineNoChangeRow(obj);
			for (int i = 0; i < length - obj.toString().length(); i++) {
				System.out.print(" ");
			}
			changeLine();
		}
	}

	/**
	 * 在控制台上换行打印对象
	 * 
	 * @param obj 要打印的对象(toString方法打印出字符串)
	 * @param length 打印完后要换的行数
	 */
	public static void writeLineAndChange(Object obj, int rows) {
		writeLineNoChangeRow(obj);
		changeLine(rows);
	}

	/**
	 * 在控制台上不换行打印对象<br>
	 * 
	 * @param str 要打印的对象(toString方法打印出字符串)
	 */
	public static void writeLineNoChangeRow(Object obj) {
		System.out.print(obj);
	}

	/**
	 * 在控制台上不换行打印对象<br>
	 * 
	 * @param str 要打印的对象(toString方法打印出字符串)
	 * @param length 打印完之后所接着的空格数
	 */
	public static void writeLineNoChangeRow(Object obj, int length) {
		length = (length < 1 ? Math.abs(length) : length);
		writeLineNoChangeRow(obj);
		String space = "";
		for (int i = 0; i < length; i++) {
			space += " ";
		}
		writeLineNoChangeRow(space);
	}

	/**
	 * 在控制台上打印一个整数值
	 * 
	 * @param num 要打印的整数值
	 */
	public static void writeLine(int num) {
		System.out.println(num);
	}

	/**
	 * 在控制台上打印一个字符
	 * 
	 * @param c 要打印的字符
	 */
	public static void writeLine(char c) {
		System.out.println(c);
	}

	/**
	 * 在控制台上打印一个浮点数
	 * 
	 * @param num 要打印的浮点数值
	 */
	public static void writeLine(double num) {
		System.out.println(num);
	}

	/**
	 * 在控制台上打印一个布尔变量
	 * 
	 * @param bool boolean
	 */
	public static void writeLine(boolean bool) {
		System.out.println(bool);
	}

	/**
	 * 用户换行
	 */
	public static void changeLine() {
		System.out.println();
	}

	/**
	 * 换行
	 * 
	 * @param lineCount 换行数目
	 */
	public static void changeLine(int lineCount) {
		lineCount = Math.abs(lineCount);
		for (int i = 0; i < lineCount; i++) {
			System.out.println();
		}
	}

	/**
	 * 在控制台中输出空格字符
	 * 
	 * @param length 要输出的空格字符的长度
	 */
	public static void writeSpace(int length) {
		length = (length < 1 ? Math.abs(length) : length);
		for (int i = 0; i < length; i++) {
			System.out.print(" ");
		}
	}

	/**
	 * 从控制台上获取一行信息
	 * 
	 * @return String 字符串信息
	 */
	public static String readLine() {
		if (in == null)
			in = new BufferedReader(new InputStreamReader(System.in));
		String info = "";
		try {
			info = in.readLine();
		} catch (IOException e) {
			throw new RecRunTimeException(e.getMessage(), e);// 转而抛出运行时异常
		}
		return info;
	}

}
