/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

import java.util.Set;

/**
 *
 * @author Acer-Lap
 */
public class REdge {
    private float length; //DISTANCE
    private float avgTravelTime;//AVGTIME
    private int node1;//NODE1
    private int node2;//NODE2
    
    private boolean isAvailable;//specifies whether the road is available at the moment of usage
    
    //These users are to be informed about congestion
    private Set<String> node1Users;
    private Set<String> node2Users;
    
    //constructor
    public REdge(float edgeLength, float travelTime, Integer firstNode, Integer secondNode){
        length = edgeLength;
        avgTravelTime = travelTime;
        node1 = firstNode;
        node2 = secondNode;
    }
    
    //method to set length
    public void setLength(int edgeLength){
        length = edgeLength;
    }
    
    //method to set avgTravelTime 
    public void setAvgTravelTime(int edgeTravelTime){
        avgTravelTime = edgeTravelTime;
    }
    
    //method to set nodes
    public void setNodes(Integer firstNode, Integer secondNode){
        
        if(!firstNode.equals(0))//no change if the argument is 0
            node1 = firstNode;
        
        if(!secondNode.equals(0))//no change if the argument is 0
            node1 = secondNode;
        
    }
         
    //method to get length
    public float getLength(){
        return length;
    }
    
    //method to get avgTravelTime 
    public float getAvgTravelTime(){
        return avgTravelTime;
    }
    
    //method to get node1
    public Integer getFirstNode(){
        return node1;
    }
    
    //method to get node2
    public Integer getSecondNode(){
        return node2;
    }
    
    //methode returns the other node if one node is given
    //useful to get the node on the other side of the edge while traversing the graph
    public Integer getNodeOnTheOtherSide(Integer nodeId){
        if(nodeId.equals(node1)){
            return node2;
        } else if(nodeId.equals(node2)){
            return node1;
        } else {
            return 0;
        }
    }
    
    //method to change the road travellability
    public void makeRoadAvailable(boolean availability){
        isAvailable = availability;
    }
    
    //method to check if the road is available at the moment for travel
    public boolean isAvailable(){
        return isAvailable;
    }
    
    //method to add users to node
    public boolean addNodeUser(Integer nodeId, String GCMId){
        if(nodeId.equals(node1)){
            node1Users.add(GCMId);
            return true;
        }
        else if (nodeId.equals(node2)){
            node2Users.add(GCMId);
            return true;
        }
        else{
            return false;
        }
    }
    
    //method to remove user from node
    public boolean removeNodeUser(Integer nodeId, String GCMId){
        if(nodeId.equals(node1)){
            node1Users.remove(GCMId);
            return true;
        }
        else if (nodeId.equals(node2)){
            node2Users.remove(GCMId);
            return true;
        }
        else{
            return false;
        }
    }

}