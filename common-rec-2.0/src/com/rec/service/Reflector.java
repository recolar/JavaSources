package com.rec.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.rec.exception.RecRunTimeException;

/**
 * ��������������
 * 
 * @author Recolar
 * @see DBProcess
 * @version 1.0.0 2006-12-10
 * @since JDK1.5.0
 * 
 */
public class Reflector {

	/**
	 * ����Class�����ȡ�ö�����������е�setter����
	 * 
	 * @param cla
	 * @return �ַ�������,�������е�setter����
	 */
	public static String[] getSetterMethodNames(Class<?> cla) {
		return innerGetMethodNames(cla, "set");
	}

	private static String[] innerGetMethodNames(Class<?> clazz, String startStr) {
		Method[] mets = clazz.getDeclaredMethods();
		String[] metNames = new String[mets.length];
		for (int i = 0; i < mets.length; i++) {
			String metName = mets[i].getName();
			if (metName.startsWith(startStr)) {
				metNames[i] = metName;
			}
		}
		return StringUtil.filterArrayOutOfNull(metNames);
	}

	/**
	 * ����Class�����ȡ�ö�����������е��ֶ�����,���ַ������鷵��
	 * 
	 * @param cla
	 * @return String[] �������е��ֶ�����
	 */
	public static String[] getDeclaredFieldName(Class<?> cla) {
		Field[] fields = cla.getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	/**
	 * ����Field���鷵�ظ�Field��Ӧ��getter����.
	 * 
	 * @param fields Field����.
	 * @return Method[]
	 */
	public static Method[] getSetterMethodsByFileds(final Field[] fields, final Class<?> clazz) {
		final Method[] methods = new Method[fields.length];
		Throwable ex = null;
		try {
			for (int i = 0; i < fields.length; i++) {
				String methodName = "set" + StringUtil.initCap(fields[i].getName());
				methods[i] = clazz.getDeclaredMethod(methodName, new Class[] { fields[i].getType() });
			}
			return methods;
		} catch (SecurityException e) {
			ex = e;
		} catch (NoSuchMethodException e) {
			ex = e;
		}
		throw new RuntimeException(ex);
	}

	/**
	 * ����Class���󷵻ظö���������������setter����
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getSetterMethodsByClass(Class<?> clazz) {
		return getSetterMethodsByFileds(clazz.getDeclaredFields(), clazz);
	}

	/**
	 * ����Class���󷵻ظö���������������getter����
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getGetterMethodsByClass(Class<?> clazz) {
		return getGetterMethodsByFileds(clazz.getDeclaredFields(), clazz);
	}

	/**
	 * ����Field���鷵�ظ�Field��Ӧ��getter����.
	 * 
	 * @param fields Field����.
	 * @return Method[]
	 */
	public static Method[] getGetterMethodsByFileds(final Field[] fields, final Class<?> clazz) {
		final Method[] methods = new Method[fields.length];
		Throwable ex = null;
		try {
			for (int i = 0; i < fields.length; i++) {
				String methodName = "get" + StringUtil.initCap(fields[i].getName());
				methods[i] = clazz.getDeclaredMethod(methodName, new Class[] {});
			}
			return methods;
		} catch (SecurityException e) {
			ex = e;
		} catch (NoSuchMethodException e) {
			ex = e;
		}
		throw new RuntimeException(ex);
	}

	/**
	 * ����Class�����ȡ�ö�����������е�getter����
	 * 
	 * @param cla
	 * @return �ַ�������,�������е�setter����
	 */
	public static String[] getGetterMethodNames(Class<?> cla) {
		return innerGetMethodNames(cla, "get");
	}

	/**
	 * ����Clas����ͷ������ƻ�ȡ�÷���������Class��������<br>
	 * ��ҪӦ����DBProcess<br>
	 * 
	 * @param cla
	 * @param methodName
	 * @return
	 */
	public static Class<?>[] getParamClass(Class<?> cla, String methodName) {
		Method[] mes = cla.getDeclaredMethods();
		for (int i = 0; i < mes.length; i++) {
			if (mes[i].getName().equalsIgnoreCase(methodName)) {
				return mes[i].getParameterTypes();
			}
		}
		return new Class[0];
	}

	/**
	 * ����һ������,ֻ����JavaBean����
	 * 
	 * @param obj
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object cloneObjByRef(Object obj) {
		Class<?> classType = obj.getClass();// ��ȡClass����
		Object tempObj = null;
		try {
			tempObj = classType.newInstance();
			Field[] fields = classType.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String getMethodName = "get" + StringUtil.initCap(fields[i].getName());
				Method getMethod = classType.getMethod(getMethodName, new Class[] {});
				Object retValue = getMethod.invoke(obj, new Object[] {});

				String setMethodName = "set" + StringUtil.initCap(fields[i].getName());
				// Method setMethod = classType.getMethod(setMethodName, new Class[] { getMethod.getReturnType() });
				Method setMethod = classType.getMethod(setMethodName, new Class[] { fields[i].getType() });
				setMethod.invoke(tempObj, new Object[] { retValue });
			}
		} catch (Exception e) {
			throw new RecRunTimeException(e.getMessage(), e);
		}
		return tempObj;
	}

	/**
	 * ��ȡĳ����������и����Class���󼯺�
	 * 
	 * @param cla ĳ�������Class����
	 * @return List �����������Class����
	 */
	public static List<Object> getSuperClassList(Class<?> cla) {
		List<Object> list = new ArrayList<Object>();
		while (cla.getSuperclass() != null) {
			cla = cla.getSuperclass();
			list.add(cla);
		}
		return list;
	}

}