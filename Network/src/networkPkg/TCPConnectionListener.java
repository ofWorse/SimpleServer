package networkPkg;

import java.io.IOException;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection, String name) throws IOException;
    void onReceiveString(TCPConnection tcpConnection, String name, String string) throws IOException;
    void onDisconnect(TCPConnection tcpConnection, String name) throws IOException;
    void onException(TCPConnection tcpConnection, Exception e); // если что-то пойдет не так
}
