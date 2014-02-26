package com.rec.web.dbutil;

import java.lang.reflect.Method;

/**
 * 在getQueryObjList方法中设置缓存的类
 * 
 * @author recolar
 * 
 */
class CacheParam {

	/**
	 * 属性名称
	 */
	private String fieldName;

	/**
	 * 属性的Class类型名称
	 */
	private String paramClassName;

	/**
	 * 属性的setter方法对应的Method实例
	 */
	private Method setterMethodObj;

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the paramClassName
	 */
	public String getParamClassName() {
		return paramClassName;
	}

	/**
	 * @param paramClassName the paramClassName to set
	 */
	public void setParamClassName(String paramClassName) {
		this.paramClassName = paramClassName;
	}

	/**
	 * @return the setterMethodObj
	 */
	public Method getSetterMethodObj() {
		return setterMethodObj;
	}

	/**
	 * @param setterMethodObj the setterMethodObj to set
	 */
	public void setSetterMethodObj(Method setterMethodObj) {
		this.setterMethodObj = setterMethodObj;
	}

}