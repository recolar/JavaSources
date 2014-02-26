package com.rec.sys.datatypeopp;

public class LongValue {

	private long value;

	public LongValue(long value) {
		this.value = value;
	}

	public long getValue() {
		return this.value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public String toString() {
		return Long.toString(this.value);
	}
}
