/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uicontroller;

import communication.Operation;
import communication.Receiver;
import communication.Request;
import communication.Response;
import communication.Sender;
import domain.City;
import domain.Message;
import domain.User;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cartman
 */
public class Controller {
    public Socket socket;
    Sender sender;
    Receiver receiver;
    private static Controller instance;
    private Controller() throws Exception{
        socket=new Socket("127.0.0.1", 9000);
        sender=new Sender(socket);
        receiver=new Receiver(socket);
    }
    public static Controller getInstance() throws Exception{
        if(instance==null){
            instance=new Controller();
        }
        return instance;
    }
    public User login(User user) throws Exception {
        Request request=new Request(Operation.LOGIN, user);
        sender.send(request);
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (User)response.getResult();
        }else{
            throw response.getException();
        }
    }

   
    
    
    public void logout(User user) throws Exception{
        Request request = new Request(Operation.LOG_OUT, user);
        sender.send(request);
       
        
    
    }
    
    public void sendMessage(Message message) throws Exception{
        Request request = new Request(Operation.SEND_TO_SPECIFIC, message);
        System.out.println("Poruka poslata"+message);
        sender.send(request);
    }
    
    public void returnOnlineUsers(User user) throws Exception {
        Request request = new Request(Operation.RETURN_LOGGED_USERS, user);
        sender.send(request);
    }
    
    public ArrayList<Message> returnAllMessages() throws Exception{
        Request request=new Request(Operation.RETURN_MESSAGE_HISTORY, "");
        sender.send(request);
        Response response=(Response)receiver.receive();
        if(response.getException()==null){
            return (ArrayList<Message>)response.getResult();
        }else{
            throw response.getException();
        }
    }

    
}
