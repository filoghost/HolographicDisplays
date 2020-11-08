package com.gmail.filoghost.holographicdisplays.util.nbt;

/**
 * The {@code TAG_Int} tag.
 */
public final class NBTInt extends NBTTag implements Cloneable {

	private int value;

	public NBTInt(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	public int getIntValue() {
		return value;
	}

	public void setIntValue(int value) {
		this.value = value;
	}

	@Override
	public NBTType getType() {
		return NBTType.INT;
	}

	// MISC

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NBTInt && equals((NBTInt) obj);
	}

	public boolean equals(NBTInt tag) {
		return this.value == tag.value;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}

	@Override
	public String toMSONString() {
		return Integer.toString(value);
	}

	@Override
	public NBTInt clone() {
		return new NBTInt(value);
	}

}
