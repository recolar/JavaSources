package com.rec.sys.datatypeopp;

public class CharValue {

	private char value;

	public CharValue(char value) {
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public String toString() {
		return Character.toString(this.value);
	}
}
