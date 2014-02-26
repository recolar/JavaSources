package com.rec.web.dbpl;

/**
 * @author recolar
 * 
 * @see ParamCon,ParamTypes
 * @version 1.0.0 2007-2.2
 * @since JDK1.4.2.6
 */
public class ParamObject {

	/**
	 * 参数下标,对应?的位置下标
	 */
	private int index;

	/**
	 * JavaBean的属性名称(大小写必须相等)
	 */
	private String typeName;

	/**
	 * 参数数据类型标识,可以使用ParamTypes的常量设置
	 */
	private int paramType;

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the paramType
	 */
	public int getParamType() {
		return paramType;
	}

	/**
	 * @param paramType the paramType to set
	 */
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

}
