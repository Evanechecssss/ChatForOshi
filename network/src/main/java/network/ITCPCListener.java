package network;

/**
 * \* https://evanechecssss.github.io
 * \
 */
public interface ITCPCListener
{
    void onConnectionReady(TCPConnection connection);
    void onReceiveString(TCPConnection connection, String string);
    void onDisconnect(TCPConnection connection);
    void onException(TCPConnection connection, Exception exception);
}
