package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ServerPinger {

	public static PingResponse fetchData(final ServerAddress serverAddress, int timeout) throws SocketTimeoutException, UnknownHostException, IOException, Exception {
		
		Socket socket = null;
		DataOutputStream dataOut = null;
		DataInputStream dataIn = null;
		
		try {
			socket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
			socket.setSoTimeout(timeout);
			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			final DataOutputStream handshake = new DataOutputStream(byteOut);
			handshake.write(0);
			PacketUtils.writeVarInt(handshake, 4);
			PacketUtils.writeString(handshake, serverAddress.getAddress(), PacketUtils.UTF8);
			handshake.writeShort(serverAddress.getPort());
			PacketUtils.writeVarInt(handshake, 1);
			byte[] bytes = byteOut.toByteArray();
			PacketUtils.writeVarInt(dataOut, bytes.length);
			dataOut.write(bytes);
			bytes = new byte[] { 0 };
			PacketUtils.writeVarInt(dataOut, bytes.length);
			dataOut.write(bytes);
			PacketUtils.readVarInt(dataIn);
			PacketUtils.readVarInt(dataIn);
			final byte[] responseData = new byte[PacketUtils.readVarInt(dataIn)];
			dataIn.readFully(responseData);
			final String jsonString = new String(responseData, PacketUtils.UTF8);
			return new PingResponse(jsonString, serverAddress);
		}
		finally {
			PacketUtils.closeQuietly(dataOut);
			PacketUtils.closeQuietly(dataIn);
			PacketUtils.closeQuietly(socket);
		}
	}

}
