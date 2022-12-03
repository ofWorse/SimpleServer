package networkPkg;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class TCPConnection {
    private final Socket socket;
    private final Thread incomingThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String address, int port) throws IOException {
        this(tcpConnectionListener, new Socket(address, port));
    }

    public TCPConnection(TCPConnectionListener tcpConnectionListener, Socket socket) throws IOException {
        this.eventListener = tcpConnectionListener;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        this.incomingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this); // передали экземпляр обромляющего класса
                    while (!incomingThread.isInterrupted()) {
                        String message = in.readLine();
                        eventListener.onReceiveString(TCPConnection.this, message);
                    }
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        this.incomingThread.start();
    }

    public synchronized void sendString(String string) {
        try {
            this.out.write(string + "\r\n");
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
