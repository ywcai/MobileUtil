package ywcai.ls.module.mouse.model.net;


import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.util.MyConfig;

public class SocketOperation implements Runnable {
    ClientSocket clientSocket;
    String ip;

    public SocketOperation(ClientSocket _clientSocket, String _ip) {
        clientSocket = _clientSocket;
        ip = _ip;
    }
    @Override
    public void run() {
        clientSocket.CreateSession(ip, MyConfig.INT_SOCKET_PORT);
    }
}
