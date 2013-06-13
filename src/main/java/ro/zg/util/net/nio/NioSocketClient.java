package ro.zg.util.net.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NioSocketClient {
    private boolean connected;
    private NioClient client;
    
    public synchronized void connect(String address, int port) throws UnknownHostException, IOException {
	if(!connected) {
	    client = new NioClient(InetAddress.getByName(address), port);
	    Thread t = new Thread(client);
	    t.setDaemon(true);
	    t.start();
	    connected=true;
	}
    }
    
    public byte[] sendAndReceive(byte[] data) throws IOException {
	RspHandler handler = new RspHandler();
	client.send(data, handler);
	return handler.getResponse();
    }
    
    public String sendAndReceive(String data) throws IOException {
	return new String(sendAndReceive(data.getBytes()));
    }
    
}
