package server;

import network.ITCPCListener;
import network.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * \* https://evanechecssss.github.io
 * \
 */
public class ChatServer implements ITCPCListener
{
    
    public static void main (String[] args)
    {
        new ChatServer();
    }
    
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    
    private ChatServer ()
    {
        System.out.println("Server running...");
        try (ServerSocket socket = new ServerSocket(8189))
        {
            while (true)
            {
                try
                {
                    new TCPConnection(this,socket.accept());
                }
                catch (IOException e)
                {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public synchronized void onConnectionReady (TCPConnection connection)
    {
        connections.add(connection);
        sendToAllConnections("Client was connected! " + connection.toString());
    }
    
    @Override
    public synchronized void onReceiveString (TCPConnection connection, String string)
    {
        sendToAllConnections(string);
    }
    
    @Override
    public synchronized void onDisconnect (TCPConnection connection)
    {
        connections.remove(connection);
        sendToAllConnections("Client was disconnect! " + connection.toString());
    }
    
    @Override
    public synchronized void onException (TCPConnection connection, Exception exception)
    {
        System.out.println("TCPConnection exception: " + exception);
        sendToAllConnections("Something is wrong! " + connection.toString() + "\n...: " + exception.getMessage());
    }
    
    private void sendToAllConnectionsBut(String string, TCPConnection c)
    {
        System.out.println(string);
        connections.forEach(connection ->
        {
            if (connection != c) {
                connection.sendString(string);
            }
        });
    }
    private void sendToAllConnections(String string)
    {
        System.out.println(string);
        connections.forEach(connection ->
        {
            connection.sendString(string);
        });
    }
}