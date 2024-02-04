/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import server.Server;

/**
 *
 * @author Cartman
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server=new Server();
        server.startServer();
    }
}
