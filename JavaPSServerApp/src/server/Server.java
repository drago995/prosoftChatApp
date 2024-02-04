/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import config.ConfigReader;
import domain.Message;
import domain.User;
import gui.ServerUI;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import logic.Controller;
import thread.ClientThread;

/**
 *
 * @author Cartman
 */
public class Server {

    private Sender sender;
    private Receiver receiver;
    private Controller controller;
    private List<ClientThread> clients;
    public ArrayList<User> loggedInUsers;
    private ServerUI serverUI;
    public int maxBrojKorisnika = 10;

    public Server() throws Exception {
        this.controller = new Controller();
        clients = new ArrayList<>();
        serverUI = new ServerUI(this);
        serverUI.setVisible(true);
        loggedInUsers = new ArrayList<>();
        //maxBrojKorisnika = Integer.parseInt(new ConfigReader().getMaxCustomers());
    }

    public void startServer() throws Exception {

        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            while (true) {
                System.out.println("Waiting for connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected!");
                ClientThread clientThread = new ClientThread(clientSocket, this);
                clientThread.start();
                clients.add(clientThread);
                //serverUI.updateTable();
                //this.printUsers();
                System.out.println(clients.size());

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean notLogin(User user) {
        for (ClientThread client : clients) {
            if (user.equals(client.getLoginUser())) {
                return false;
            }
        }
        return true;
    }

    public void logOutUser(User user) {
        Iterator<ClientThread> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientThread client = iterator.next();
            if (user.equals(client.getLoginUser())) {
                iterator.remove();  // Safely remove the element using the iterator
                serverUI.removeUser(user);
            }
        }

    }

    public void LogInUser(User user) {
        //loggedInUsers.add(user);
        serverUI.addUser(user);
    }

    public void broadcast(Message message) throws Exception {
        for (ClientThread ct : clients) {
            if (ct != null) {
                ct.sendMessage(message);
            }
        }
    }
    
    public void sendToSpecific(Message message) throws Exception{
        for(ClientThread ct : clients){
            if (ct != null && message.getPrimalac().getFirstname().equals(ct.getLoginUser().getFirstname())){
                
                ct.sendMessage(message);
            }
        
        }
        
    }

    public void sendOnlineUsers() throws Exception {
        
        ArrayList<User> users = new ArrayList<>();
        for (ClientThread ct : clients) {
             if (ct != null) {
                users.add(ct.getLoginUser());
            } 
        }
        for (ClientThread ct : clients) {
             if (ct != null) {
                ct.sendOnlineUsers(users);
            } 
        }
    }

}
