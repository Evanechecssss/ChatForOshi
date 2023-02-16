package com.evanechecssss.girlai.client;

import com.evanechecssss.girlai.client.utile.RandomNickGen;

import network.ITCPCListener;
import network.TCPConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * \* https://evanechecssss.github.io
 * \
 */
public class ClientWindow extends JFrame implements ActionListener, ITCPCListener
{
    private static final String IP_ADDR = "localhost";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("");
    private final JTextField fieldInput = new JTextField("");
    private TCPConnection connection;
    private RandomNickGen gen;
    
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run ()
            {
                new ClientWindow();
            }
        });
    }

    
    private ClientWindow()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setTitle("Чатик тест");
        gen = new RandomNickGen();
        fieldNickname.setText(gen.getRandom());
        log.setVisible(true);
        log.setLineWrap(true);
        add(log,BorderLayout.CENTER);
        fieldInput.addActionListener(this);
        add(fieldNickname,BorderLayout.NORTH);
        add(fieldInput,BorderLayout.SOUTH);
        setVisible(true);
        try
        {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        }
        catch (IOException exception)
        {
            printMessage("Connection Exception: " + exception.getMessage());
        }
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        String message = fieldInput.getText();
        if (message.equals(""))
        {
            return;
        }
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText() + " -> " + message);
    }
    
    private synchronized void printMessage (String message)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                log.append(message + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
    
    @Override
    public void onConnectionReady (TCPConnection connection) {
        printMessage("User was connected: " + connection.toString());
    }
    
    @Override
    public void onReceiveString (TCPConnection connection, String string) {
        printMessage(string);
    }
    
    @Override
    public void onDisconnect (TCPConnection connection) {
        printMessage("User was disconnected: " + connection.toString());
    }
    
    @Override
    public void onException (TCPConnection connection, Exception exception) {
        printMessage("Connection Exception: " + exception.getMessage());
    }
}