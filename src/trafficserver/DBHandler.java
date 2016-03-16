/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Ajilal
 */
public class DBHandler {
    //The connection object
    private Connection connection;
    
    //constructor loads the JDBC driver
    public DBHandler(){
        String driverName = "com.mysql.jdbc.Driver";//for mysql
        try{
            //load JDBC driver
            Class.forName(driverName).newInstance();
            connection = null;
        } catch(ClassNotFoundException e){
            System.out.println("\nClass not found: " + e.getMessage());
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Connect to the database with username and password
    //returns true if the connection established successfully
    public boolean connect(String username, String password){
        //url of the database
        String url = "jdbc:mysql://localhost/itcas";
        
        try {
            //Establishing the connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    //mehtod to close the connection
    //should be called while terminating the admin interface
    public void closeConnection(){
        try {
            if(connection != null)
                connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //method to get the node id from name string
    public Integer getNodeId(String name) throws SQLException{
        Statement statement = connection.createStatement();
        String query = "SELECT NID FROM NODES WHERE NAME=" + name;
        ResultSet result = statement.executeQuery(query);
        
        if(result == null){
            return null;
        } else {
            Integer id = result.getInt("NID");
            return id;
        }
    }
    
    //node Hash map creator
    //return null if any error occurs
    public HashMap<Integer, RNode> createNodeHashMap() throws IOException, ClassNotFoundException{
        if(connection == null)
            return null;
        
        try {
            //the hashmap variables
            HashMap<Integer, RNode> nodeMap = new HashMap();
            RNode node;
            Integer nodeId;
            
            //Querying the database
            Statement statement = connection.createStatement();
            String query = "SELECT NID,NDNAME,LATITUDE,LONGITUDE,INEDGE,OUTEDGE FROM NODES";
            ResultSet result = statement.executeQuery(query);
            
            if(result == null)
                   return null;
            
            //Temporary variables
            Set<Integer> inEdges;
            Set<Integer> outEdges;
            
            //variables for deserialisation
            byte[] tempBuffer;
            ObjectInputStream oiStream;
            
            while(result.next()){
                nodeId = result.getInt("NID");
                node = new RNode(result.getString("NDNAME"),result.getDouble("LATITUDE"), result.getDouble("LONGITUDE"));
                 
                //to deserialise the inedge data
                tempBuffer = result.getBytes("INEDGE");
                oiStream = new ObjectInputStream(new ByteArrayInputStream(tempBuffer));
                inEdges = (Set<Integer>) oiStream.readObject();
                node.setInEdges(inEdges);
                
                //to deserialise the outedge data
                tempBuffer = result.getBytes("OUTEDGE");
                oiStream = new ObjectInputStream(new ByteArrayInputStream(tempBuffer));
                inEdges = (Set<Integer>) oiStream.readObject();
                node.setInEdges(inEdges);
                
                //add the node into the map
                nodeMap.put(nodeId, node);
            }
            
            return nodeMap;
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //node Hash map creator
    //return null if any error occurs
    public HashMap<Integer, REdge> createEgeHashMap() throws IOException, ClassNotFoundException{
        if(connection == null)
            return null;
        
        try {
            //the hashmap variables
            HashMap<Integer, REdge> edgeMap = new HashMap();
            REdge edge;
            Integer edgeId;
            
            //Querying the database
            Statement statement = connection.createStatement();
            String query = "SELECT EID,DISTANCE,AVGTIME,NODE1,NODE2 FROM EDGES";
            ResultSet result = statement.executeQuery(query);
            
            if(result == null)
                   return null;
            
            while(result.next()){
                edgeId = result.getInt("EID");
                edge = new REdge(result.getFloat("DISTANCE"), result.getFloat("AVGTIME"), result.getInt("NODE1"), result.getInt("NODE2"));
                
                //add the node into the map
                edgeMap.put(edgeId, edge);
            }
            
            return edgeMap;
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
