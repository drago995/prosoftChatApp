/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import domain.City;
import domain.Message;
import domain.User;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import repository.db.DatabaseBroker;
import repository.db.DatabaseConnection;


/**
 *
 * @author Cartman
 */
public class Controller {
    DatabaseBroker dbb;
    
    public Controller() throws Exception{
        try {
            dbb = new DatabaseBroker(DatabaseConnection.getInstance().pop());
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
   public User login(User user) throws Exception {
    User userFromDb = dbb.getUser(user);
    
    if (userFromDb == null) {
        throw new Exception("Korisnik ne postoji.");
    }

    // Additional validation if needed
    return userFromDb;
}

    public List<Message> getAllMessages() throws Exception {
        
        return dbb.getAllMessages();
    } 

    public void saveMessage(Message message) {
       dbb.saveMessage(message);
    }
}
