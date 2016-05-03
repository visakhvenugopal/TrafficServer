/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer-Lap
 */
public class ServerAdmin {

    public DBHandler db;
    public GraphHandler gh;

    SensorCommunicator sensorCom = null ;
    ClientCommunicator clientCom = null ;       

    public ServerAdmin() {
        db = new DBHandler();
        init();
    }

    public void init() {
        String username = "root";
        String password = "";
        if (db.connect(username, password)) {
            System.out.println("\nConnection successfull..");
        } else {
            System.out.println("\nConnection failed..");
        }
        
        gh = new GraphHandler(db);
        System.out.println(GraphHandler.edgeMap.get(510).getAvgTravelTime());
        System.out.println(GraphHandler.nodeMap.get(110).getNodeName());
    }
    
    public void startCommunication()
    {
        sensorCom = new SensorCommunicator();
        sensorCom.start();
        clientCom = new ClientCommunicator();
        clientCom.start();
    }

}
