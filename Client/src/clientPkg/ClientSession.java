package clientPkg;

import networkPkg.TCPConnection;
import networkPkg.TCPConnectionListener;

import java.io.IOException;
import java.util.Scanner;

import static networkSettingsPkg.ServerSettings.IP_ADR;
import static networkSettingsPkg.ServerSettings.PORT;

public class ClientSession implements TCPConnectionListener {

    private TCPConnection connection;
    private String name;

    public void connect() {
        try {
            tryToRegister();
            while (true) {
                sendMSG();
            }
        } catch (IOException e) {
            log("TCPConnection exception: " + e);
        }
    }

    private void tryToRegister() throws IOException {
        log("Ввведите имя пользователя: ");
        this.name = new Scanner(System.in).nextLine();
        this.connection = new TCPConnection(this, name, IP_ADR, PORT);
    }

    public void sendMSG() {
        String msg = new Scanner(System.in).nextLine();
        if(msg.equals("")) return;
        connection.sendString((name.isEmpty() ? "User" : name) + ": ", msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection, String name) {
        log("\nСоединение готово...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String name, String string) {
        log(string);
    }


    @Override
    public void onDisconnect(TCPConnection tcpConnection, String name) {
        log("Соединение прервано.");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        log("TCPConnection exception: " + e);
    }

    private synchronized void log(String message) {
        System.out.println(message);
    }
}
