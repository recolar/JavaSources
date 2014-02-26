package com.rec.sys.datatypeopp;

public class BooleanValue {

	private boolean value;

	public BooleanValue(boolean value) {
		this.value = value;
	}

	public String toString() {
		return Boolean.toString(this.value);
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
}
