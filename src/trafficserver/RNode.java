package trafficserver;

import java.util.Set;

public class RNode {
    public static final int IN_EDGE = 1;
    public static final int OUT_EDGE = 2;
    
    private String name;
    private double latitude;
    private double longitude;
    
    private Set<Integer> inEdges;
    private Set<Integer> outEdges;
    
    private float trafficIntensityIndex;
    
    //methods to set values
    //The constructor initialises the nodes with its id, name, latitude and longitude
    public RNode(String nodeName, double lat, double longi){
        latitude = lat;
        longitude = longi;
        name = nodeName;
    }
    
    //method to set the in edges
    public void setInEdges(Set<Integer> edgeArray){
       inEdges.addAll(edgeArray);
    }
    
    //method to set the out edge array
    public void setOutEdges(Set<Integer> edgeArray){
       outEdges.addAll(edgeArray);
    }
    
    //method to clear inedges
    public void clearInEdges(int edgeSet){
        if(edgeSet == RNode.IN_EDGE)
            inEdges.clear();
        else if (edgeSet == RNode.OUT_EDGE)
            outEdges.clear();
    }
    
    //method to change traffic intensity index
    public void setTrafficIntensityindex(float newIndex){
        trafficIntensityIndex = newIndex;
    }
    
    //method to set node name
    public void setNodeName(String newName){
        name = newName;
    }
    
    //methods to get the values
    //method to retrive latitude
    public double getLat(){
        return latitude;
    }
    
    //method to retrive latitude
    public double getLon(){
        return longitude;
    }
    
    //method to get the edge set
    public Set<Integer> getEdgeSet(int edgeSet){
        if(edgeSet == RNode.IN_EDGE)
            return inEdges;
        else if(edgeSet == RNode.OUT_EDGE)
            return outEdges;
        else
            return null;
    }
    
    
    //method to change traffic intensity index
    public float getTrafficIntensityindex(){
        return trafficIntensityIndex;
    }
    
    //method to get the node name
    public String getNodeName(){
        return name;
    }
    
}
