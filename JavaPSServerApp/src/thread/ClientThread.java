/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thread;

import communication.Operation;
import static communication.Operation.LOG_OUT;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import domain.Message;
import domain.User;
import java.net.Socket;
import java.util.ArrayList;
import logic.Controller;
import server.Server;

/**
 *
 * @author student2
 */
public class ClientThread extends Thread {

    private final Socket clientSocket;
    private Sender sender;
    private Receiver receiver;
    private Controller controller;
    private User loginUser;
    public Server server;
    private boolean exit = false;

    public ClientThread(Socket clientSocket, Server server) throws Exception {
        this.clientSocket = clientSocket;
        sender = new Sender(clientSocket);
        receiver = new Receiver(clientSocket);
        controller = new Controller();
        this.server = server;
    }

    @Override
    public void run() {

        while (!isInterrupted()) {
            try {
                Request request = (Request) receiver.receive();
                Response response = new Response();

                try {
                    switch (request.getOperation()) {
                        case LOGIN:
                            User user = (User) request.getArgument();
                            if (server.maxBrojKorisnika > 0) {
                                if (server.notLogin(user)) {
                                    response.setResult(controller.login(user));
                                    loginUser = user;
                                    server.LogInUser(user);
                                    server.maxBrojKorisnika--;
                                    sender.send(response);
                                    //sender.send(server.loggedInUsers);
                                } else {
                                    throw new Exception("User je vec prijavljen.");
                                    //to do
                                }

                            } else {
                                throw new Exception("Server je pun");
                            }

                            break;
                        case LOG_OUT:
                            User user1 = (User) request.getArgument();
                            
                            logOutUser();
                            server.logOutUser(user1);
                            
                            //sender.send(response);
                            System.out.println("STIZE LI OVDE ");
                            shutdown();
                            break;
                        case RETURN_MESSAGE_HISTORY:
                            System.out.println("Vracam istoriju poruka");
                            response.setResult(controller.getAllMessages());
                            sender.send(response);
                            break;
                        // sve ostale podatke saljemo sve vreme, sem kad je log in ili log out    
                        case RETURN_LOGGED_USERS:
                            System.out.println("hej hoj");
                            server.sendOnlineUsers();
                            continue;
                        case SEND_TO_SPECIFIC:
                            Message message = (Message) request.getArgument();
                            controller.saveMessage(message);
                            server.sendToSpecific(message);
                            continue;
                            
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setException(e); 
                    sender.send(response);
                }
                

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public User getLoginUser() {
        return loginUser;
    }

    private void shutdown() {
        interrupt();
    }

    public void sendMessage(Message message) throws Exception {
        Request request = new Request(Operation.SEND_TO_SPECIFIC, message);
        sender.send(request);
    }

    public void sendOnlineUsers(ArrayList<User> users) throws Exception {
           Request request = new Request(Operation.RETURN_LOGGED_USERS, users );
           sender.send(request);
    }
    
    public void logOutUser() throws Exception{
    
    Request request = new Request(Operation.LOG_OUT, null);
    sender.send(request);
    
    }
    

    

}
