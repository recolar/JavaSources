package com.rec.sys.datatypeopp;

public class ShortValue {

	private short value;

	public ShortValue(short value) {
		this.value = value;
	}

	public short getValue() {
		return this.value;
	}

	public void setValue(short value) {
		this.value = value;
	}

	public String toString() {
		return Short.toString(value);
	}

}
