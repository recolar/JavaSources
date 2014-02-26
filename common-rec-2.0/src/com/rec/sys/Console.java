package com.rec.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rec.exception.RecRunTimeException;

/**
 * �����̨��ӡ�ַ�������
 * 
 * @author Recolar
 */
public class Console {

	private static BufferedReader in;

	/**
	 * �ڿ���̨�ϻ��д�ӡ����<br>
	 * 
	 * @param obj Ҫ��ӡ�Ķ���(toString������ӡ���ַ���)
	 */
	public static void writeLine(Object obj) {
		System.out.println(obj);
	}

	/**
	 * �ڿ���̨�ϻ��д�ӡ����
	 * 
	 * @param obj Ҫ��ӡ�Ķ���(toString������ӡ���ַ���)
	 * @param length ��ӡ���еĿ��
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
	 * �ڿ���̨�ϻ��д�ӡ����
	 * 
	 * @param obj Ҫ��ӡ�Ķ���(toString������ӡ���ַ���)
	 * @param length ��ӡ���Ҫ��������
	 */
	public static void writeLineAndChange(Object obj, int rows) {
		writeLineNoChangeRow(obj);
		changeLine(rows);
	}

	/**
	 * �ڿ���̨�ϲ����д�ӡ����<br>
	 * 
	 * @param str Ҫ��ӡ�Ķ���(toString������ӡ���ַ���)
	 */
	public static void writeLineNoChangeRow(Object obj) {
		System.out.print(obj);
	}

	/**
	 * �ڿ���̨�ϲ����д�ӡ����<br>
	 * 
	 * @param str Ҫ��ӡ�Ķ���(toString������ӡ���ַ���)
	 * @param length ��ӡ��֮�������ŵĿո���
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
	 * �ڿ���̨�ϴ�ӡһ������ֵ
	 * 
	 * @param num Ҫ��ӡ������ֵ
	 */
	public static void writeLine(int num) {
		System.out.println(num);
	}

	/**
	 * �ڿ���̨�ϴ�ӡһ���ַ�
	 * 
	 * @param c Ҫ��ӡ���ַ�
	 */
	public static void writeLine(char c) {
		System.out.println(c);
	}

	/**
	 * �ڿ���̨�ϴ�ӡһ��������
	 * 
	 * @param num Ҫ��ӡ�ĸ�����ֵ
	 */
	public static void writeLine(double num) {
		System.out.println(num);
	}

	/**
	 * �ڿ���̨�ϴ�ӡһ����������
	 * 
	 * @param bool boolean
	 */
	public static void writeLine(boolean bool) {
		System.out.println(bool);
	}

	/**
	 * �û�����
	 */
	public static void changeLine() {
		System.out.println();
	}

	/**
	 * ����
	 * 
	 * @param lineCount ������Ŀ
	 */
	public static void changeLine(int lineCount) {
		lineCount = Math.abs(lineCount);
		for (int i = 0; i < lineCount; i++) {
			System.out.println();
		}
	}

	/**
	 * �ڿ���̨������ո��ַ�
	 * 
	 * @param length Ҫ����Ŀո��ַ��ĳ���
	 */
	public static void writeSpace(int length) {
		length = (length < 1 ? Math.abs(length) : length);
		for (int i = 0; i < length; i++) {
			System.out.print(" ");
		}
	}

	/**
	 * �ӿ���̨�ϻ�ȡһ����Ϣ
	 * 
	 * @return String �ַ�����Ϣ
	 */
	public static String readLine() {
		if (in == null)
			in = new BufferedReader(new InputStreamReader(System.in));
		String info = "";
		try {
			info = in.readLine();
		} catch (IOException e) {
			throw new RecRunTimeException(e.getMessage(), e);// ת���׳�����ʱ�쳣
		}
		return info;
	}

}
