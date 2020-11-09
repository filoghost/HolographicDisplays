package me.filoghost.holographicdisplays.util.nbt;

/**
 * The {@code TAG_String} tag.
 */
public final class NBTString extends NBTTag implements Cloneable {

	private String value;

	public NBTString(String value) {
		setValue(value);
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public NBTType getType() {
		return NBTType.STRING;
	}

	// MISC

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toMSONString() {
		return toMSONString(value);
	}

	@Override
	public NBTString clone() {
		return new NBTString(value);
	}

	// UTIL

	/**
	 * Converts a regular string into a Mojangson string by surrounding it with quotes and escaping backslashes and
	 * quotes inside it.
	 *
	 * @param str the string
	 * @return the Mojangson string
	 */
	public static String toMSONString(String str) {
		StringBuilder builder = new StringBuilder("\"");
		char[] chars = str.toCharArray();
		for (char c : chars) {
			if ((c == '\\') || (c == '"')) {
				builder.append('\\');
			}
			builder.append(c);
		}
		return builder.append('\"').toString();
	}

}
