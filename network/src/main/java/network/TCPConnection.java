package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * \* https://evanechecssss.github.io
 * \
 */
public class TCPConnection
{
    private final Socket socket;
    private final Thread rxThread;
    private final ITCPCListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;
    
    public TCPConnection(ITCPCListener listener, String ip, int port ) throws IOException
    {
        this(listener, new Socket(ip, port));
    }
    
    public TCPConnection(ITCPCListener listener, Socket socket) throws IOException
    {
        this.socket = socket;
        this.listener = listener;
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
        rxThread = new Thread(new Runnable()
        {
            @Override
            public void run ()
            {
                try
                {
                    listener.onConnectionReady(TCPConnection.this);
                    while (!rxThread.isInterrupted())
                    {
                        listener.onReceiveString(TCPConnection.this, in.readLine());
                    }
        
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
        
                }
            }
        });
        rxThread.start();
    }
    
    public synchronized void sendString(String string)
    {
        try {
            
            out.write(string + "\r\n");
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            disconnect();
        }
    }
    
    public synchronized void disconnect()
    {
        rxThread.interrupt();
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            listener.onException(this, e);
        }
    }
    
    @Override
    public String toString () {
        return "TCPConnection " + socket.getInetAddress() + " : " + socket.getPort();
    }
}