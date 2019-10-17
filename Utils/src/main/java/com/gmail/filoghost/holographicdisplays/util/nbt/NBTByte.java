package com.gmail.filoghost.holographicdisplays.util.nbt;

/**
 * The {@code TAG_Byte} tag.
 */
public final class NBTByte extends NBTTag implements Cloneable {

	private byte value;

	public NBTByte(byte value) {
		this.value = value;
	}

	@Override
	public Byte getValue() {
		return value;
	}

	public byte getByteValue() {
		return value;
	}

	public void setByteValue(byte value) {
		this.value = value;
	}

	@Override
	public NBTType getType() {
		return NBTType.BYTE;
	}

	// MISC

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NBTByte && equals((NBTByte) obj);
	}

	public boolean equals(NBTByte tag) {
		return this.value == tag.value;
	}

	@Override
	public int hashCode() {
		return Byte.hashCode(value);
	}

	@Override
	public String toMSONString() {
		return Byte.toUnsignedInt(value) + "b";
	}

	@Override
	public NBTByte clone() {
		return new NBTByte(value);
	}

}
