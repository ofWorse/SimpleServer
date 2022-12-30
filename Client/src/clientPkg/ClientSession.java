package clientPkg;

import namesPkg.NamesHolder;
import networkPkg.TCPConnection;
import networkPkg.TCPConnectionObserver;


import java.io.IOException;
import java.util.Scanner;

import static networkSettingsPkg.ServerSettings.IP_ADR;
import static networkSettingsPkg.ServerSettings.PORT;

public class ClientSession implements TCPConnectionObserver {

    private TCPConnection connection;
    private String name;

    public void connect() {
        try {
            tryToRegister();
            while (true) {
                sendMSG();
            }
        } catch (IOException e) {
            logLn("TCPConnection exception: " + e);
        }
    }

    private void tryToRegister() throws IOException {
        var a = false;
        while(!a) {
            logLn("Ввведите имя пользователя: ");
            this.name = new Scanner(System.in).nextLine();
            if(NamesHolder.addUser(name)) a = true;
            else logLn("Такое имя уже существует, или вы ввели запрещенное имя.");
        }
        this.connection = new TCPConnection(this, name, IP_ADR, PORT);
    }

    public void sendMSG() throws IOException {
        String msg = new Scanner(System.in).nextLine();
        if (msg.equals("")) return;
        connection.sendString(name + ": ", msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection, String name) {
        logLn("\nСоединение готово...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String name, String string) {
        logLn(string);
    }


    @Override
    public void onDisconnect(TCPConnection tcpConnection, String name) {
        logLn("Соединение прервано.");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        logLn("TCPConnection exception: " + e);
    }

    private synchronized void logLn(String message) {
        System.out.println(message);
    }
}
