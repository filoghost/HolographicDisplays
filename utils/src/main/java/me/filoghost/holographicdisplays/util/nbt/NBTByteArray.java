package me.filoghost.holographicdisplays.util.nbt;

import java.util.Arrays;

/**
 * The {@code TAG_Byte_Array} tag.
 */
public final class NBTByteArray extends NBTTag {

	private final byte[] value;

	public NBTByteArray(byte[] value) {
		this.value = value;
	}

	public NBTByteArray(Number[] numbers) {
		this.value = new byte[numbers.length];
		for (int i = 0; i < numbers.length; i++)
			value[i] = numbers[i].byteValue();
	}

	/**
	 * Returns the length of this array.
	 *
	 * @return the length of this array
	 */
	public int length() {
		return value.length;
	}

	@Override
	public byte[] getValue() {
		return value;
	}

	@Override
	public NBTType getType() {
		return NBTType.BYTE_ARRAY;
	}

	// MISC

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NBTByteArray && equals((NBTByteArray) obj);
	}

	public boolean equals(NBTByteArray tag) {
		return Arrays.equals(this.value, tag.value);
	}

	@Override
	public String toMSONString() {
		StringBuilder stringbuilder = new StringBuilder("[B;");
		for (int i = 0; i < this.value.length; i++) {
			if (i != 0) {
				stringbuilder.append(',');
			}
			stringbuilder.append(this.value[i]).append('B');
		}
		return stringbuilder.append(']').toString();
	}

}
