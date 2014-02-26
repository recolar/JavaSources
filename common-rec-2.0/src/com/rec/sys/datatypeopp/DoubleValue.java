package com.rec.sys.datatypeopp;

public class DoubleValue {

	private double value;

	public DoubleValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String toString() {
		return Double.toString(this.value);
	}
}
