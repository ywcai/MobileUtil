package ywcai.ls.common.net;

import java.net.InetSocketAddress;
import java.net.Socket;

import ywcai.ls.util.statics.MyConfig;

public class SocketTest {
    public Boolean GetRemotePcStatus(String ip) {
        try {
            Socket socket = new Socket();
            InetSocketAddress inetSocketAddress=new InetSocketAddress(ip,MyConfig.INT_SOCKET_PORT);
            socket.connect(inetSocketAddress,3000);
            socket.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
