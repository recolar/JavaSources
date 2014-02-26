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
	 * �����±�,��Ӧ?��λ���±�
	 */
	private int index;

	/**
	 * JavaBean����������(��Сд�������)
	 */
	private String typeName;

	/**
	 * �����������ͱ�ʶ,����ʹ��ParamTypes�ĳ�������
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
