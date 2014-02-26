package com.rec.web.dbutil;

import java.lang.reflect.Method;

/**
 * ��getQueryObjList���������û������
 * 
 * @author recolar
 * 
 */
class CacheParam {

	/**
	 * ��������
	 */
	private String fieldName;

	/**
	 * ���Ե�Class��������
	 */
	private String paramClassName;

	/**
	 * ���Ե�setter������Ӧ��Methodʵ��
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