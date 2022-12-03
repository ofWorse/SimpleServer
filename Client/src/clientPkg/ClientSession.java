package clientPkg;

import networkPkg.TCPConnection;
import networkPkg.TCPConnectionListener;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class ClientSession implements TCPConnectionListener {
    private static final String IP_ADR = "192.168.1.83";
    private static final int PORT = 8189;
    private final String name;

    public ClientSession(String name) {
        this.name = name;
    }

    private TCPConnection connection;

    public void connect() {
        try {
            this.connection = new TCPConnection(this, IP_ADR, PORT);
            while (true) {
                sendMSG();
            }
        } catch (IOException e) {
            log("TCPConnection exception: " + e);
        }
    }

    public void sendMSG() {
        String msg = new Scanner(System.in).nextLine();
        if(msg.equals("")) return;
        connection.sendString("[" + new Date() + "] " + name + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        log("Соединение готово...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String string) {
        log(string);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        log("Соединение прервано.");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private synchronized void log(String message) {
        System.out.println(message);
    }
}
