package trafficserver;

import com.google.android.gcm.server.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.graphstream.graph.Node;

public class RNode {

    //public static final int IN_EDGE = 1;
    //public static final int OUT_EDGE = 2;
    public int id;
    private String name;
    private final double latitude;
    private final double longitude;
    private boolean overridden;

    //private List<Integer> inEdges;
    private String[] outEdges;

    //These users are to be informed about congestion
    private Map<String, Long> nodeUsers;
    /*This map defines the time dependent delay on the node
     * this data is stored as time-delay pair
     * the time delay is stored for every hour in a day
     */
    private Map<Integer, Integer> timeDependentDelay;

    private int lastUpdateTime;

    private boolean isNodeAvailable;

    private int congestionIndex;

    private int curationTime;

    //methods to set values
    //The constructor initialises the nodes with its id, name, latitude and longitude
    public RNode(String nodeName, double lat, double longi) {
        latitude = lat;
        longitude = longi;
        name = nodeName;
        overridden = false;

        outEdges = null;

        timeDependentDelay = new HashMap<>();
        for(int i = 0; i < 24; i++){
            timeDependentDelay.put(i, 0);
        }

        lastUpdateTime = 0;
        isNodeAvailable = true;
        congestionIndex = 0;

        nodeUsers = new HashMap<>();
    }

    public void setCurationTime(int ttc) {
        curationTime = ttc;
    }

    public boolean getOverridden()
    {
        return overridden ;
    }
    
    public void setOverridden(boolean state)
    {
        overridden = state ;
        
    }
    
    public int getCurationTime() {
        return curationTime;
    }

    public void setCongestionIndex(int index) {
        congestionIndex = index;
    }

    public int getCongestionIndex() {
        return congestionIndex;
    }

    public void setNodeAvailability(boolean isAvailable) {
        isNodeAvailable = isAvailable;
    }

    public boolean getNodeAvailability() {
        return isNodeAvailable;
    }

    public void setLastUpdateTime(int time) {
        lastUpdateTime = time;
    }

    public int getLastUpdateTime() {
        return lastUpdateTime;
    }

    //method to set the timeDependent Delay
    public void setDelay(Map<Integer, Integer> delayMap) {
        timeDependentDelay.putAll(delayMap);
    }

    public void setTimeDependentDelay(int time, int delay) {
        timeDependentDelay.put(time, delay);
    }

    //method to set the out edge array
    public void setEdgeList(String[] edgeArray) {
        outEdges = edgeArray;
    }

    //method to clear inedges
    public void clearEdgeSet() {
        outEdges = null;
    }

    //method to set node name
    public void setNodeName(String newName) {
        name = newName;
    }

    //methods to get the values
    //method to retrive latitude
    public double getLat() {
        return latitude;
    }

    //method to retrive latitude
    public double getLon() {
        return longitude;
    }

    //method to get the edge set
    public String[] getEdgeList() {
        return outEdges;
    }

    //method to get the node name
    public String getNodeName() {
        return name;
    }

    //method to get time dependent delay at the node
    public int getDelayAt(int time) {
        time = time / 100;//time is given in 24hr format. we need only the hr part not the minute part
        return timeDependentDelay.get(time);
    }

    //method to inform the node users about the congestion
    public boolean informCongestion() {

        Iterator iterator;
        String tempGCMId;

        if (nodeUsers.isEmpty()) {
            return true;
        }

        Set<String> userList = nodeUsers.keySet();
        long arrivalTime;
        iterator = userList.iterator();

        long currentTime = (System.currentTimeMillis() / (60 * 1000));
        while (iterator.hasNext()) {
            tempGCMId = (String) iterator.next();
            arrivalTime = nodeUsers.get(tempGCMId);
            //new Notifier(tempGCMId).start();
            
            if ((arrivalTime - currentTime) <= (curationTime)) {
                new Notifier(tempGCMId).start();
            }
        }

        return true;
    }

    //method to add users to node
    public void addNodeUser(String GCMId, long arrivalTime) {
        nodeUsers.put(GCMId, arrivalTime);
    }

    //method to remove user from node
    public void removeNodeUser(String GCMId) {
        nodeUsers.remove(GCMId);
    }

    private class Notifier extends Thread {

        String GCMID;

        public Notifier(String GCM) {
            super();
            GCMID = GCM;
        }

        @Override
        public void run() {
            if(congestionIndex == 0)
                return;
            
            List<String> androidTargetList;
            String collapseKey;
            collapseKey = "congestionAt" + id;
            String timeToCure = "" + curationTime;
            androidTargetList = new ArrayList<>();

            androidTargetList.add(GCMID);
            Message.Builder builder = new Message.Builder();
            builder.addData("key", "congestion");
            builder.addData("node", "" + id);
            builder.addData("ttc", timeToCure);
            builder.addData("user", GCMID);
            //create message
            //buld the message
            Message message = builder.build();

            //post message
            GCMBroadcast gcm = new GCMBroadcast();
            gcm.broadcast(androidTargetList, collapseKey, message);
        }
    }
}
