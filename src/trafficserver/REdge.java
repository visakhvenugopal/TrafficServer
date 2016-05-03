package trafficserver;

public class REdge {

    final private float length; //DISTANCE in km
    private int avgTravelTime;//AVGTIME in min
    private int node1;//NODE1
    private int node2;//NODE2


    private boolean isAvailable;//specifies whether the road is available at the moment of usage

    //these randomiser variables are auto incremented from 0 to 20 on addition of each node user
    //after 20, it returns to 0. The randomiser variables
    //aparently incraeses the avg travel time of the edge 
    //this is useful to distribute the traffic load almost evenly
    private int node1Randomiser;
    private int node2Randomiser;

    //constructor
    public REdge(float edgeLength, int travelTime, Integer firstNode, Integer secondNode) {
        length = edgeLength;
        avgTravelTime = travelTime;
        node1 = firstNode;
        node2 = secondNode;
        isAvailable = true;//specifies whether the road is available at the moment of usage


        node1Randomiser = 0;
        node2Randomiser = 0;

    }

    //method to set avgTravelTime 
    public void setAvgTravelTime(int edgeTravelTime) {
        avgTravelTime = edgeTravelTime;
    }

    //method to set nodes
    public void setNodes(Integer firstNode, Integer secondNode) {

        if (!firstNode.equals(0))//no change if the argument is 0
        {
            node1 = firstNode;
        }

        if (!secondNode.equals(0))//no change if the argument is 0
        {
            node2 = secondNode;
        }

    }
    
    //method to set the availability of nodes
    public void setNodeAvailable(Integer nodeId, boolean isAvailable) {

        GraphHandler.nodeMap.get(nodeId).setNodeAvailability(isAvailable);
    }

    //method to check if the specified node is avilable
    public boolean isNodeAvailable(Integer nodeId) {
        return GraphHandler.nodeMap.get(nodeId).getNodeAvailability();
    }

    //method to get length
    public float getLength() {
        return length;
    }

    //method to get avgTravelTime 
    public float getAvgTravelTime() {
        return avgTravelTime;
    }

    //method to get node1
    public Integer getFirstNode() {
        return node1;
    }

    //method to get node2
    public Integer getSecondNode() {
        return node2;
    }

    //methode returns the other node if one node is given
    //useful to get the node on the other side of the edge while traversing the graph
    public Integer getNodeOnTheOtherSide(Integer nodeId) {
        if (nodeId.equals(node1)) {
            return node2;
        } else if (nodeId.equals(node2)) {
            return node1;
        } else {
            return 0;
        }
    }

    //method to change the road travellability
    public void makeRoadAvailable(boolean availability) {
        isAvailable = availability;
    }

    //method to check if the road is available at the moment for travel
    public boolean isAvailable() {
        return isAvailable;
    }
    //method to return travelTime
   /* public long getTravelTimeTo(Integer nodeId) {
        int addedWeight = 0;

        if (nodeId.equals(node1)) {
            addedWeight = node1Randomiser + GraphHandler.nodeMap.get(node1).getCurationTime();
        } else if (nodeId.equals(node2)) {
            addedWeight = node2Randomiser + GraphHandler.nodeMap.get(node2).getCurationTime();
        }

        return avgTravelTime + addedWeight;
    }*/

    //method to return travelTime
    public int getTravelTimeTo(Integer nodeId, int time) {
        int rand = 0;

        if (nodeId.equals(node1)) {
            rand = node1Randomiser;
        } else if (nodeId.equals(node2)) {
            rand = node2Randomiser;
        }

        return avgTravelTime + rand + GraphHandler.nodeMap.get(nodeId).getDelayAt(computeTimeIn24HrFormat(time, avgTravelTime));
    }

    private int computeTimeIn24HrFormat(int currTime, int timeToAddMin) {
        int minFactor = currTime - (int) (currTime / 100);
        int addedMins = minFactor + timeToAddMin;

        if (addedMins < 60) {
            currTime = (int) (currTime / 100) + addedMins;
        } else if (addedMins == 60) {
            currTime = currTime + 100;
        } else {
            currTime = (int) ((currTime + 100) / 100);
            addedMins = addedMins - (60 - minFactor);

            currTime = currTime + (100 * (int) (addedMins / 60));
            currTime = currTime + (addedMins % 60);
        }

        currTime = currTime % 1200;

        return currTime;
    }
    
    public void updateNodeRandomiser(int nodeId){
        if(nodeId == node1){
            node1Randomiser = (node1Randomiser + 2) % 20;
        } else if (nodeId == node2){
            node2Randomiser = (node2Randomiser + 2) % 20;
        }
    }
}
