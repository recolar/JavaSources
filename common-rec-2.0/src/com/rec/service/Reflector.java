package com.rec.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.rec.exception.RecRunTimeException;

/**
 * 操作反射对象的类
 * 
 * @author Recolar
 * @see DBProcess
 * @version 1.0.0 2006-12-10
 * @since JDK1.5.0
 * 
 */
public class Reflector {

	/**
	 * 根据Class对象获取该对象下面的所有的setter方法
	 * 
	 * @param cla
	 * @return 字符串数组,保存所有的setter方法
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
	 * 根据Class对象获取该对象下面的所有的字段名称,以字符串数组返回
	 * 
	 * @param cla
	 * @return String[] 保存所有的字段名称
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
	 * 根据Field数组返回该Field对应的getter方法.
	 * 
	 * @param fields Field数组.
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
	 * 根据Class对象返回该对象所声明的所有setter方法
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getSetterMethodsByClass(Class<?> clazz) {
		return getSetterMethodsByFileds(clazz.getDeclaredFields(), clazz);
	}

	/**
	 * 根据Class对象返回该对象所声明的所有getter方法
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getGetterMethodsByClass(Class<?> clazz) {
		return getGetterMethodsByFileds(clazz.getDeclaredFields(), clazz);
	}

	/**
	 * 根据Field数组返回该Field对应的getter方法.
	 * 
	 * @param fields Field数组.
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
	 * 根据Class对象获取该对象下面的所有的getter方法
	 * 
	 * @param cla
	 * @return 字符串数组,保存所有的setter方法
	 */
	public static String[] getGetterMethodNames(Class<?> cla) {
		return innerGetMethodNames(cla, "get");
	}

	/**
	 * 根据Clas对象和方法名称获取该方法参数的Class对象数组<br>
	 * 主要应用于DBProcess<br>
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
	 * 复制一个对象,只限于JavaBean对象
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
		Class<?> classType = obj.getClass();// 获取Class对象
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
	 * 获取某个对象的所有父类的Class对象集合
	 * 
	 * @param cla 某个对象的Class对象
	 * @return List 包括多个父类Class对象
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