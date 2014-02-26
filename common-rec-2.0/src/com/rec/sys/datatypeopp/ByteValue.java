package com.rec.sys.datatypeopp;

public class ByteValue {

	private byte value;

	public ByteValue(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public String toString() {
		return Byte.toString(this.value);
	}
}
