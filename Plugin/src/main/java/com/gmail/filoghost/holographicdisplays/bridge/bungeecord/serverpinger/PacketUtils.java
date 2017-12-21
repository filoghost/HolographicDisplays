package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.lang.RuntimeException;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.Character;
import java.lang.String;
import java.io.DataOutputStream;
import java.nio.charset.Charset;

class PacketUtils
{
    public static final Charset UTF16BE = Charset.forName("UTF-16BE");;
    public static final Charset UTF8 = Charset.forName("UTF-8");
    
    public static void a(final DataOutputStream out, final String s) throws IOException {
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        out.write(data);
    }
    
    public static void writeString(final DataOutputStream out, final String s, final Charset charset) throws IOException {
        if (charset == PacketUtils.UTF8) {
            writeVarInt(out, s.length());
        }
        else {
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
		} catch (IOException e) { }
    }

}
