package networkPkg;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection, String name);
    void onReceiveString(TCPConnection tcpConnection, String name, String string);
    void onDisconnect(TCPConnection tcpConnection, String name);
    void onException(TCPConnection tcpConnection, Exception e); // если что-то пойдет не так
}
