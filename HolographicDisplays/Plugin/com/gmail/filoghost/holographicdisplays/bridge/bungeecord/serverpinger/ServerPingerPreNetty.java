package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.lang.Override;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.lang.Integer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.lang.String;

final class ServerPingerPreNetty extends ServerPinger {
	
	@Override
	public ServerStatus fetchData(final ServerAddress serverAddress, int timeout) throws SocketTimeoutException, UnknownHostException, IOException, Exception {
		
		Socket socket = null;
		DataOutputStream dataOut = null;
		DataInputStream dataIn = null;
		
		try {
			socket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
			socket.setSoTimeout(timeout);
			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			PacketUtils.a(dataOut, "FE");
			PacketUtils.a(dataOut, "01");
			dataIn.readByte();
			dataIn.readByte();
			final int length = dataIn.readByte() * 2;
			final byte[] bytes = new byte[length];
			dataIn.readFully(bytes);
			socket.close();
			final String[] info = new String(bytes, PacketUtils.UTF16BE).split(String.valueOf('\0'));
			final ServerStatus response = new ServerStatus(true, info[3], Integer.parseInt(info[4]), Integer.parseInt(info[5]));
			// String versionName = info[2];
			// String protocol = info[1];
			return response;
		}
		finally {
			PacketUtils.closeQuietly(dataIn);
			PacketUtils.closeQuietly(dataOut);
			PacketUtils.closeQuietly(socket);
		}
	}
}
