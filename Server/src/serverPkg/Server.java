package serverPkg;

import networkPkg.TCPConnection;
import networkPkg.TCPConnectionListener;
import networkSettingsPkg.ServerSettings;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server implements TCPConnectionListener {
    public static void main(String[] args) {
        new Server();
    }

    private final List<TCPConnection> connectionList = new ArrayList<>();

    private Server() {
        System.out.println("Сервер запущен...");
        try (var serverSocket = new ServerSocket(ServerSettings.PORT)) {
            while (true) {
                try {
                    new TCPConnection(this, "Server_By_Mark_M", serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO чтобы выводило имя пользователя а не сервера!
    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection, String name) {
        connectionList.add(tcpConnection);
        sendToAll(name, "Клиент " + tcpConnection + " подключился.");
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String name, String string) {
        sendToAll(name, string);
    }

    // TODO чтобы выводило имя пользователя а не сервера!
    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection, String name) {
        connectionList.remove(tcpConnection);
        sendToAll(name, "Клиент " + tcpConnection + " отключился.");
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAll(String name, String string) {
        System.out.println("\n"+ "[" + new Date() + "] " + name + " : " + string);
        for(var user : connectionList)
            user.sendString(name +"\n"+ "[" + new Date() + "] ", string);
    }
}













