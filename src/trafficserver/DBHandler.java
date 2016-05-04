package trafficserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Set;

public class DBHandler {

    //The connection object

    private static Connection connection;

    //constructor loads the JDBC driver
    public DBHandler() {
    }

    //Connect to the database with username and password
    //returns true if the connection established successfully
    public boolean connect(String username, String password) {
        //url of the database
        String url = "jdbc:mysql://localhost/itcas";
        String driverName = "com.mysql.jdbc.Driver";//for mysql
        try {
            //load JDBC driver
            Class.forName(driverName).newInstance();
                        connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException e) {
            System.out.println("\nClass not found: " + e.getMessage());
            return false;
        } catch (InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    //mehtod to close the connection
    //should be called while terminating the admin interface
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //method to get the node id from name string
    public Integer getNodeId(String name) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT NID FROM node_table WHERE NdName=" + name;
        ResultSet result = statement.executeQuery(query);
        

        if (result == null) {
            return null;
        } else {
            result.next();
            Integer id = result.getInt("NID");
            return id;
        }
    }

    //node Hash map creator
    //return null if any error occurs
    public HashMap<Integer, RNode> createNodeHashMap() throws IOException, ClassNotFoundException {
        if (connection == null) {
            return null;
        }

        try {
            //the hashmap variables
            HashMap<Integer, RNode> nodeMap = new HashMap();
            RNode node;
            Integer nodeId;

            //Querying the database
            Statement statement = connection.createStatement();
            String query = "SELECT NID,NdName,Lat,Lon,OutEdge FROM node_table";
            ResultSet result = statement.executeQuery(query);

            if (result == null) {
                return null;
            }

            //Temporary variables
            //Set<Integer> inEdges;
            //String[] outEdges;

            //variables for deserialisation
            String[] edgeList;
            ObjectInputStream oiStream;

            while (result.next()) {
                nodeId = result.getInt("NID");
                node = new RNode(result.getString("NdName"), Double.parseDouble(result.getString("Lat")), Double.parseDouble(result.getString("Lon")));

                //to deserialise the inedge data
                /*tempBuffer = result.getBytes("INEDGE");
                 oiStream = new ObjectInputStream(new ByteArrayInputStream(tempBuffer));
                 inEdges = (Set<Integer>) oiStream.readObject();
                 node.setInEdges(inEdges);
                 */
                //to deserialise the outedge data
                edgeList = result.getString("OutEdge").split(" ");
                node.setEdgeList(edgeList);

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
    public HashMap<Integer, REdge> createEdgeHashMap() throws IOException, ClassNotFoundException {
        if (connection == null) {
            return null;
        }

        try {
            //the hashmap variables
            HashMap<Integer, REdge> edgeMap = new HashMap();
            REdge edge;
            Integer edgeId;

            //Querying the database
            Statement statement = connection.createStatement();
            String query = "SELECT EID,Distance,TIME,ND1,ND2 FROM edge_table";
            ResultSet result = statement.executeQuery(query);

            if (result == null) {
                return null;
            }

            while (result.next()) {
                edgeId = result.getInt("EID");
                edge = new REdge(result.getFloat("Distance"), result.getInt("TIME"), result.getInt("ND1"), result.getInt("ND2"));

                //add the node into the map
                edgeMap.put(edgeId, edge);
            }

            return edgeMap;
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    //returns the nearest nodes to the end points
    public int[] getNearestNodes(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        int[] endNodes = new int[2];
        double currentBestDistance = Double.MAX_VALUE;
        /*double dispFact = 0.06;
        
        int nodeId;
        double currentDistance;
        double latitude;
        double longitude;
        
        try {
            String query = "SELECT NID,Lat,Lon FROM node_table WHERE (Lat BETWEEN ? AND ?) AND (Lon BETWEEN ? AND ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, startLatitude + dispFact);
            statement.setDouble(2, startLatitude - dispFact);
            statement.setDouble(3, endLatitude + dispFact);
            statement.setDouble(4, endLatitude - dispFact);
            //finding the starting node
            
            ResultSet result = statement.executeQuery();
            //continue the query with a high displacement factor untill we get a non empty result set
            while(result == null){
                dispFact += 0.01;
                query = "SELECT NID,Lattitude,Longitude FROM node_table WHERE (Lattitude BETWEEN " + (startLatitude + dispFact) + " AND " + (startLatitude - dispFact) + ") AND (Longitude BETWEEN " + (startLongitude + dispFact) + " AND " + (startLongitude - dispFact) +")";
                result = statement.executeQuery(query);
            }
            
            while(result.next()){
                nodeId = result.getInt("NID");
                latitude = Double.parseDouble(result.getString("Lattitude"));
                longitude = Double.parseDouble(result.getString("Longitude"));
                
                currentDistance = distanceInKMeters(startLatitude, startLongitude, latitude, longitude);
                
                if(currentDistance < currentBestDistance){
                    endNodes[0] = nodeId;
                    currentBestDistance = currentDistance;
                }
            }
            
            //finding the ending node
            dispFact = 0.06;
            currentBestDistance = Double.MAX_VALUE;
            query = "SELECT NID,Lattitude,Longitude FROM node_table WHERE (Lattitude BETWEEN " + (endLatitude + dispFact) + " AND " + (endLatitude - dispFact) + ") AND (Longitude BETWEEN " + (endLongitude + dispFact) + " AND " + (endLongitude - dispFact) +")";
            result = statement.executeQuery(query);
            //continue the query with a high displacement factor untill we get a non empty result set
            while(result == null){
                dispFact += 0.01;
                query = "SELECT NID,Lattitude,Longitude FROM node_table WHERE (Lattitude BETWEEN " + (endLatitude + dispFact) + " AND " + (endLatitude - dispFact) + ") AND (Longitude BETWEEN " + (endLongitude + dispFact) + " AND " + (endLongitude - dispFact) +")";
                result = statement.executeQuery(query);
            }
            
            while(result.next()){
                nodeId = result.getInt("NID");
                latitude = Double.parseDouble(result.getString("Lattitude"));
                longitude = Double.parseDouble(result.getString("Longitude"));
                
                currentDistance = distanceInKMeters(startLatitude, startLongitude, latitude, longitude);
                
                if(currentDistance < currentBestDistance){
                    endNodes[1] = nodeId;
                    currentBestDistance = currentDistance;
                }
            }
            
            return endNodes;
            
        } catch (SQLException ex) {
            return null;
        }*/
        Set<Integer> keyList = GraphHandler.nodeMap.keySet();
        double currDist;
        for(Integer node: keyList){
            currDist = Math.sqrt((GraphHandler.nodeMap.get(node).getLat() - startLatitude)*(GraphHandler.nodeMap.get(node).getLat() - startLatitude) +
                    (GraphHandler.nodeMap.get(node).getLon() - startLongitude)*(GraphHandler.nodeMap.get(node).getLon() - startLongitude));
            if(currDist < currentBestDistance){
                currentBestDistance = currDist;
                endNodes[0] = node;
            }
        }
        
        currentBestDistance = Double.MAX_VALUE;
        for(Integer node: keyList){
            currDist = Math.sqrt((GraphHandler.nodeMap.get(node).getLat() - endLatitude)*(GraphHandler.nodeMap.get(node).getLat() - endLatitude) +
                    (GraphHandler.nodeMap.get(node).getLon() - endLongitude)*(GraphHandler.nodeMap.get(node).getLon() - endLongitude));
            if(currDist < currentBestDistance){
                currentBestDistance = currDist;
                endNodes[1] = node;
            }
        }
        System.out.println("" + endNodes[0] + " " + endNodes[1]);
        return endNodes;
    }
    
    private double distanceInKMeters(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    
    //node Hash map creator
    //return null if any error occurs
    public boolean updateUserNodeList(int userId, String nodeList) throws IOException, ClassNotFoundException {
        if (connection == null) {
            return false;
        }

        try {
            //Querying the database
            PreparedStatement statement = connection.prepareStatement("SELECT Nodes, GCM_id FROM user_table WHERE UID=?");
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();

            if (result == null) {
                return false;
            }
            result.next();
            String currentNodeList = result.getString("Nodes");
            String GCMID = result.getString("GCM_id");
            if(currentNodeList.equals(nodeList)){
                return false;
            } else{
                String[] ndList = currentNodeList.split(" ");
                int ndId;
                for(String ndIdString: ndList){
                    ndId = Integer.parseInt(ndIdString);
                    GraphHandler.nodeMap.get(ndId).removeNodeUser(GCMID);
                }
                
                statement = connection.prepareStatement("UPDATE user_table SET Nodes=? WHERE UID=?");
                statement.setString(1, nodeList);
                statement.setInt(2, userId);
                statement.executeUpdate();
                return true;
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public int addNewUser(String GCMID){
        if (connection == null) {
            return Integer.MAX_VALUE;
        }

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user_table WHERE GCM_id=?");
            statement.setString(1, GCMID);
            if(statement.executeQuery().next())
                return Integer.MAX_VALUE;

            statement = connection.prepareStatement("INSERT INTO user_table(GCM_id, Nodes)VALUES(?,?)");
            statement.setString(1, GCMID);
            statement.setString(2, "0");
            statement.executeUpdate();

            statement = connection.prepareStatement("SELECT UID FROM USER_TABLE WHERE GCM_id=?");
            statement.setString(1, GCMID);
            ResultSet result = statement.executeQuery();
            result.next();  
            return result.getInt("UID");
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return Integer.MAX_VALUE;
        }
    }
    
    public String getGCMId(int userId){
       if (connection == null) {
            return null;
        }

        try {
            //Querying the database
            PreparedStatement statement = connection.prepareStatement("SELECT GCM_id FROM user_table WHERE UID=?");
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();

            if (result == null) {
                return null;
            }
            result.next();
            return result.getString("GCM_id");
           
        } catch (SQLException ex) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }
}
