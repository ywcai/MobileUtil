package ywcai.ls.common.net;


import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.util.statics.MyConfig;

public class CreateSocket implements Runnable {
    ClientSocket clientSocket;
    String ip;

    public CreateSocket(ClientSocket _clientSocket, String _ip) {
        clientSocket = _clientSocket;
        ip = _ip;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientSocket.CreateSession(ip, MyConfig.INT_SOCKET_PORT);
    }
}
