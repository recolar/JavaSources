package com.rec.sys.datatypeopp;

public class IntValue {

	private int value;

	public IntValue(int value) {
		this.value = value;
	}

	public String toString() {
		return Integer.toString(this.value);
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
