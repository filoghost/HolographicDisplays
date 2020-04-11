/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.lang.RuntimeException;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.String;
import java.io.DataOutputStream;
import java.nio.charset.Charset;

class PacketUtils {
	
	public static final Charset UTF8 = Charset.forName("UTF-8");

	public static void writeString(final DataOutputStream out, final String s, final Charset charset) throws IOException {
		if (charset == PacketUtils.UTF8) {
			writeVarInt(out, s.length());
		} else {
			out.writeShort(s.length());
		}
		out.write(s.getBytes(charset));
	}

	public static int readVarInt(final DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			final int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
			if ((k & 0x80) != 0x80) {
				return i;
			}
		}
	}

	public static void writeVarInt(final DataOutputStream out, int paramInt) throws IOException {
		while ((paramInt & 0xFFFFFF80) != 0x0) {
			out.write((paramInt & 0x7F) | 0x80);
			paramInt >>>= 7;
		}
		out.write(paramInt);
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {}
	}

}
