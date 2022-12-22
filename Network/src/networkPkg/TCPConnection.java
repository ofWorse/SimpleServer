package networkPkg;

import namesPkg.NamesHolder;
import serializePkg.ReadObject;
import serializePkg.WriteObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final Thread incomingThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;
    public String name;

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String name, String address, int port) throws IOException {
        this(tcpConnectionListener, name, new Socket(address, port));
    }

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String name, Socket socket) throws IOException {
        this.name = name;
        this.eventListener = tcpConnectionListener;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        this.incomingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this, name); // передали экземпляр обромляющего класса
                    while (!incomingThread.isInterrupted()) {
                        String message = in.readLine();
                        eventListener.onReceiveString(TCPConnection.this, name, message);
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    try {
                        eventListener.onDisconnect(TCPConnection.this, name);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        this.incomingThread.start();
    }

    public synchronized void sendString(String name, String string) {
        try {
            new WriteObject((name.isEmpty() ? "User" : name) + string + "\r\n");
            this.out.write(new ReadObject().readOb());
            this.out.flush();
        } catch (IOException e) {
            this.eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        this.incomingThread.interrupt();
        try {
            this.socket.close();
        } catch (IOException e) {
            this.eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
