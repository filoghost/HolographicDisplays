package com.gmail.filoghost.holographicdisplays.bridge.bungeecord.serverpinger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public abstract class ServerPinger {
	
	// For 1.7 and higher
    public static final ServerPinger POST_NETTY_REWRITE = new ServerPingerPostNetty();
    
    // For 1.6 and lower
    public static final ServerPinger PRE_NETTY_REWRITE = new ServerPingerPreNetty();
    
    public abstract ServerStatus fetchData(final ServerAddress serverAddress, int timeout) throws SocketTimeoutException, UnknownHostException, IOException, Exception;
    
}
