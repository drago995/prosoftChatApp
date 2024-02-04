/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uicontroller;

import communication.Receiver;
import communication.Request;
import communication.Sender;
import domain.Message;
import domain.User;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import ui.form.FrmMain;

/**
 *
 * @author Dragomir
 */
public class Client extends Thread {
    User user;
    Socket socket;
    Sender sender;
    Receiver receiver;
    public JTextField inbox;
    public JComboBox onlineUsers;
    public JTextField sendMessage;
    
    
    
    public Client(User user1, Socket socket1, JTextField txtInbox, JComboBox<Object> cmbOnlineUsers, JTextField txtMessageText, FrmMain aThis) {
        
        user = user1;
        socket = socket1;
        inbox = txtInbox;
        onlineUsers = cmbOnlineUsers;
        sendMessage = txtMessageText;
        receiver = new Receiver(socket);
        
        
    }
    
    
    @Override
    public void run(){
    
        while(!isInterrupted()){
            try {
                Request request = (Request) receiver.receive();
                switch(request.getOperation()){
                    
                    case RETURN_LOGGED_USERS:
                        List<User> users = (List<User>) request.getArgument();
                        onlineUsers.setModel(new DefaultComboBoxModel(users.toArray()));
                        continue;

                    case SEND_TO_SPECIFIC:
                        Message message1 = (Message) request.getArgument();
                        inbox.setText(inbox.getText() + "\n"+ message1.getPosaljilac().getUsername()+":"+message1.getTekst());
                        continue;
                    case LOG_OUT:
                        interrupt();
                        
                        
                
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        }
    
    
    
    }
    
}
  