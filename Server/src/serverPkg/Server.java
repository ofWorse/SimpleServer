package serverPkg;

import networkPkg.TCPConnection;
import networkPkg.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }

    private final List<TCPConnection> connectionList = new ArrayList<>();
    private Server() {
        System.out.println("Сервер запущен...");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());// объект socket при входящем соединении
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connectionList.add(tcpConnection);
        sendToAll("Клиент подключился: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String string) {
        sendToAll(string);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connectionList.remove(tcpConnection);
        sendToAll("Клиент отключился: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAll(String string) {
        System.out.println(string);
        for(var user : connectionList)
            user.sendString(string);
    }
}













