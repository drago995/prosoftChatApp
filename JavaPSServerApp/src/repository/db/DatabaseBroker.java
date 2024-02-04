/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository.db;

import domain.City;
import domain.DomainObject;
import domain.Message;
import domain.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cartman
 */
public class DatabaseBroker {

    private final Connection connection;

    public DatabaseBroker(Connection connection) {
        this.connection = connection;
    }

    public User getUser(User user) throws SQLException {
        try {
            String query = "SELECT userid, firstname, lastname, username, password FROM user1 WHERE username=? AND password=?";
            System.out.println("Upit: " + query);

            //Pravljenje objekta koji je odgovoran za izvrsavanje upita
            PreparedStatement statement
                    = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            //izvsi upit
            ResultSet rs = statement.executeQuery();

            //pristup rezultatima upita
            if (rs.next()) {
                user.setUserID(rs.getLong("userid"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
            } else {
                user = null;
            }
            //oslobadjanje resursa
            rs.close();
            statement.close();
            System.out.println("Uspesno ucitavanje objekta User iz baze!");
            return user;
        } catch (SQLException ex) {
            System.out.println("Objekat User nije uspesno ucitan iz baze!");
            ex.printStackTrace();
            throw ex;
        }
    }

    public void add(DomainObject domainObject) throws SQLException {
        try {
//            String query = "INSERT INTO person (firstname, lastname, birthday, city, gender, married) VALUES (?,?,?,?,?,?)";
            String query = "INSERT INTO " + domainObject.getTableName()
                    + " (" + domainObject.getColumnsForInsert() + ") VALUES " + domainObject.getParamsForInsert();

            System.out.println("Upit: " + query);

            //Pravljenje objekta koji je odgovoran za izvrsavanje upita
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            //postavljanje vrednosti parametara
            domainObject.setParamsForInsert(statement, domainObject);

            //izvsi upit
            int result = statement.executeUpdate();
            //System.out.println("Result = " + result);
            System.out.println("Objekat uspesno dodat u bazu!");

            //pristup generisanom kljucu
            if (domainObject.containsAutoIncrementPK()) {
                ResultSet rsID = statement.getGeneratedKeys();
                if (rsID.next()) {
                    //person.setPersonID(rsID.getLong(1));
                    domainObject.setAutoIncrementPrimaryKey(rsID.getLong(1));
                }
                rsID.close();
            }
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Neuspesno dodavanje objekta u bazu!\n" + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    public List<Message> getAllMessages() {
        try {
            List<Message> messages = new ArrayList<>();
            String query = "SELECT * FROM user1 AS u JOIN message AS m ON u.userid = m.user ORDER BY m.date";
            System.out.println("Upit: " + query);

            //Pravljenje objekta koji je odgovoran za izvrsavanje upita
            Statement statement = connection.createStatement();
            //izvsi upit
            ResultSet rs = statement.executeQuery(query);

            //pristup rezultatima upita
            while (rs.next()) {
                Message message = new Message();
                User sender = new User();
                sender.setFirstname(rs.getString("firstname"));
                sender.setLastname(rs.getString("lastname"));
                sender.setUsername(rs.getString("username"));
                message.setPosaljilac(sender);
                message.setTekst(rs.getString("text"));
                message.setDatum(rs.getDate("date"));
                // message.setName(rs.getString("name"));//city.setName(rs.getString(2));
                messages.add(message);
            }
            //oslobadjanje resursa
            rs.close();
            statement.close();
            System.out.println("Uspesno ucitavanje objekata City iz baze!");
            return messages;
        } catch (SQLException ex) {
            System.out.println("Objekti City nisu uspesno ucitani iz baze!");
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void saveMessage(Message message) {
         String query = "INSERT INTO message (text, user, date) VALUES (?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, message.getTekst());
            statement.setLong(2, message.getPosaljilac().getUserID());
            statement.setTimestamp(3, new java.sql.Timestamp(message.getDatum().getTime()));

            
           int result =  statement.executeUpdate();
           connection.commit();
            System.out.println("Objekat uspesno dodat u bazu!");
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseBroker.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
         
         
    }

}
