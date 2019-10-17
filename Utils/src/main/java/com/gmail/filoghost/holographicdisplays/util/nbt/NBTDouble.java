package com.gmail.filoghost.holographicdisplays.util.nbt;

/**
 * The {@code TAG_Double} tag.
 */
public final class NBTDouble extends NBTTag implements Cloneable {

	private double value;

	public NBTDouble(double value) {
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public double getDoubleValue() {
		return value;
	}

	public void setDoubleValue(double value) {
		this.value = value;
	}

	@Override
	public NBTType getType() {
		return NBTType.DOUBLE;
	}

	// MISC

	@Override
	public boolean equals(Object obj) {
		return obj instanceof NBTDouble && equals((NBTDouble) obj);
	}

	public boolean equals(NBTDouble tag) {
		return this.value == tag.value;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(value);
	}

	@Override
	public String toMSONString() {
		return value + "d";
	}

	@Override
	public NBTDouble clone() {
		return new NBTDouble(value);
	}

}