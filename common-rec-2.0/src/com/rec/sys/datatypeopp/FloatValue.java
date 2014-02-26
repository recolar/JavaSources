package com.rec.sys.datatypeopp;

public class FloatValue {

	private float value;

	public FloatValue(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String toString() {
		return Float.toString(this.value);
	}
}
