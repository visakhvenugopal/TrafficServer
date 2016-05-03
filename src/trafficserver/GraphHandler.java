/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

import com.google.android.gcm.server.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer-Lap
 */
public class GraphHandler {

    public static HashMap<Integer, REdge> edgeMap;
    public static HashMap<Integer, RNode> nodeMap;

    private static DBHandler db;

    private Map<Integer, SubNodes> subGraph;//hash map to store sub graph

    //Subnodes are arranged according to its heuristic value
    private SubNodes priorityList;
    private int user;

    //indicates the positions of the currently processing node in the likedlist    
    private static final int SINGLE_ELEMENT = 0;
    private static final int LEFT_END = 1;
    private static final int RIGHT_END = 2;
    private static final int MIDDLE = 3;

    public GraphHandler(int userId) {
        user = userId;
    }
    public GraphHandler(DBHandler dbh) {
       initMap(dbh);
    }

    //method to initialise the graph
    static public void initMap(DBHandler dbH) {
        try {
            db = dbH;
            nodeMap = db.createNodeHashMap();
            edgeMap = db.createEdgeHashMap();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GraphHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //method to organise the routing
    public Message.Builder getRout(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        try {
            int[] nearestNodes = null;
            
            nearestNodes = db.getNearestNodes(startLatitude, startLongitude, endLatitude, endLongitude);
            System.out.println(nearestNodes);
            populateNodeMap(nearestNodes[0], nearestNodes[1]);
            
            return returnRout(nearestNodes[0], nearestNodes[1]);
        } catch (IOException ex) {
            Logger.getLogger(GraphHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //method that populates the edgeset to be travelled through
    private void populateNodeMap(int startNode, int endNode) {
        if (startNode != endNode) {
            RNode node = GraphHandler.nodeMap.get(startNode);
            RNode end = GraphHandler.nodeMap.get(endNode);
            REdge currentEdge;

            int tempNodeId;
            long tempTotalTimeEstimation;
            long tempTimeElapsed;
            long currentBestTime = Integer.MAX_VALUE;

            subGraph = new HashMap<>();

            SubNodes currentNode = new SubNodes(startNode, node.getLat(), node.getLon(), node.getEdgeList());
            SubNodes eNode = new SubNodes(endNode, end.getLat(), end.getLon(), end.getEdgeList());
            SubNodes nextNode;
            
            int edgeId;

            currentNode.distanceTraveled = 0;
            currentNode.timeElapsed = 1;

            subGraph.put(startNode, currentNode);
            priorityList = currentNode;
            

            while (priorityList != null) {
                currentNode = priorityList;
                //process the edges on byone
                for (String edgeIdString : currentNode.outEdges){
                    edgeId = Integer.parseInt(edgeIdString);
                    currentEdge = GraphHandler.edgeMap.get(edgeId);
                    if (currentEdge.isAvailable()) {
                        tempNodeId = currentEdge.getNodeOnTheOtherSide(currentNode.id);
                        if ((tempNodeId == endNode) || currentEdge.isNodeAvailable(tempNodeId)) {
                            if (subGraph.containsKey(tempNodeId)) {
                                nextNode = subGraph.get(tempNodeId);
                                tempTimeElapsed = currentNode.timeElapsed + currentEdge.getTravelTimeTo(tempNodeId, (int) (((System.currentTimeMillis() / 60000) % (60 *24)) + currentNode.timeElapsed));
                                tempTotalTimeEstimation = tempTimeElapsed + ((long) (distanceInKMeters(nextNode.lat, nextNode.lon, eNode.lat, eNode.lon)
                                        / (currentNode.distanceTraveled / currentNode.timeElapsed)));
                                if (tempTotalTimeEstimation < nextNode.totalTimeEstimation) {
                                    nextNode.distanceTraveled = currentNode.distanceTraveled + ((int) currentEdge.getLength() * 1000);
                                    nextNode.timeElapsed = tempTimeElapsed;
                                    nextNode.totalTimeEstimation = tempTotalTimeEstimation;
                                    nextNode.parent = currentNode;

                                    //the node must be replaced in the linked list
                                    //first, it must be taken out of the list
                                    switch (getNodePositionInTheList(nextNode)) {
                                        case MIDDLE:
                                            nextNode.next.prev = nextNode.prev;
                                            nextNode.prev.next = nextNode.next;
                                            break;
                                        case LEFT_END:
                                            nextNode.prev.next = null;
                                            break;
                                        case RIGHT_END:
                                            priorityList = nextNode.next;
                                            nextNode.next.prev = null;
                                            break;
                                        case SINGLE_ELEMENT:
                                            priorityList = null;
                                            break;
                                    }
                                    //add the nextNode to the subGraph if the otal time estimation is good
                                    if (nextNode.totalTimeEstimation < currentBestTime) {
                                        addNodeToPriorityList(nextNode);
                                    }
                                }
                            } else {
                                node = GraphHandler.nodeMap.get(tempNodeId);
                                nextNode = new SubNodes(tempNodeId, node.getLat(), node.getLon(), node.getEdgeList());
                                nextNode.distanceTraveled = currentNode.distanceTraveled + ((int) currentEdge.getLength() * 1000);
                                nextNode.timeElapsed = currentNode.timeElapsed + currentEdge.getTravelTimeTo(tempNodeId, (int) (((System.currentTimeMillis() / 60000) % (60 *24)) + currentNode.timeElapsed));
                                nextNode.totalTimeEstimation = nextNode.timeElapsed + ((long) (distanceInKMeters(nextNode.lat, nextNode.lon, eNode.lat, eNode.lon)
                                        / (currentNode.distanceTraveled / currentNode.timeElapsed)));
                                nextNode.parent = currentNode;

                                //add the nextNode to the subGraph if the otal time estimation is good
                                if (nextNode.totalTimeEstimation < currentBestTime) {
                                    subGraph.put(tempNodeId, nextNode);
                                    addNodeToPriorityList(nextNode);
                                }
                            }
                            //update the current best time if the node is the destination
                            if (tempNodeId == endNode) {
                                nextNode.outEdges = null;//no need to travel from the end node
                                if (nextNode.totalTimeEstimation < currentBestTime) {
                                    currentBestTime = nextNode.totalTimeEstimation;
                                }
                            }
                        }
                    }
                }

                //remove current node from the list
                switch (getNodePositionInTheList(currentNode)) {
                    case MIDDLE:
                        currentNode.next.prev = currentNode.prev;
                        currentNode.prev.next = currentNode.next;
                        break;
                    case LEFT_END:
                        currentNode.prev.next = null;
                        break;
                    case RIGHT_END:
                        priorityList = currentNode.next;
                        currentNode.next.prev = null;
                        break;
                    case SINGLE_ELEMENT:
                        priorityList = null;
                        break;
                }
            }
        }

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

    private int getNodePositionInTheList(SubNodes node) {
        if (node.next == null) {
            if (node.prev == null) {//only one element
                return SINGLE_ELEMENT;
            } else {//left end
                return LEFT_END;
            }
        } else if (node.prev == null) {//right end
            return RIGHT_END;
        }

        return MIDDLE;
    }

    private void addNodeToPriorityList(SubNodes nextNode) {
        //add nextNode to the list
        //find the position to be added to
        SubNodes prevNode = null;
        SubNodes iterator = priorityList;
        while (iterator != null) {
            if (iterator.totalTimeEstimation > nextNode.totalTimeEstimation) {
                break;
            }
            prevNode = iterator;
            iterator = iterator.next;
        }
        if (iterator == null) {
            if (prevNode == null) {
                priorityList = nextNode;
                nextNode.prev = nextNode.next = null;
            } else {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                nextNode.next = null;
            }
        } else {
            if (prevNode == null) {
                nextNode.next = iterator;
                nextNode.prev = null;
                iterator.prev = nextNode;
                priorityList = nextNode;
            } else {
                nextNode.prev = prevNode;
                prevNode.next = nextNode;
                nextNode.next = iterator;
                iterator.prev = nextNode;
            }
        }
    }

    private class SubNodes {

        int id;
        public double lat;
        public double lon;

        public int distanceTraveled;
        public long timeElapsed;

        public long totalTimeEstimation;

        public String[] outEdges;

        public SubNodes parent;

        public SubNodes next;
        public SubNodes prev;

        public SubNodes(int nodeId, double latitude, double longitude, String[] edges) {
            id = nodeId;
            lat = latitude;
            lon = longitude;

            outEdges = edges;
            
            next = null;
            prev = null;
            parent = null;
        }
    }

    private Message.Builder returnRout(int startNode, int endNode) throws IOException {
        try {
            System.out.println("Inside returnRout..");
            int nodeId = endNode;
            int nodeCount = 0;
            
            String nodeList = "";
            //create message
            Message.Builder builder = new Message.Builder();
            while (nodeId != startNode) {
                if (subGraph.containsKey(nodeId)) {
                    nodeCount++;
                    builder.addData("" + nodeCount, nodeMap.get(nodeId).getNodeName());
                    builder.addData("ar" + nodeCount, "" + subGraph.get(nodeId).timeElapsed);
                    builder.addData("la" + nodeCount, "" + subGraph.get(nodeId).lat);
                    builder.addData("lo" + nodeCount, "" + subGraph.get(nodeId).lon);
                    
                    nodeList = nodeList + nodeId + " ";//separated by space
                    
                    nodeId = subGraph.get(nodeId).parent.id;
                } else {
                    System.out.println("!subGraph.containsKey("+nodeId+")&" + subGraph.keySet().toString());
                    
                    return null;
                }
            }
            
            nodeCount++;
            builder.addData("" + nodeCount, nodeMap.get(startNode).getNodeName());
            builder.addData("a" + nodeCount, "" + subGraph.get(startNode).timeElapsed);
            builder.addData("la" + nodeCount, "" + subGraph.get(startNode).lat);
            builder.addData("lo" + nodeCount, "" + subGraph.get(startNode).lon);
            builder.addData("count", "" + nodeCount);
            
            nodeList = nodeList + startNode;
            
            if(!db.updateUserNodeList(user, nodeList)){
                return null;
            }
            
            //add user to nodelist
            for(String nodeIdString: nodeList.split(" ")){
                nodeId = Integer.parseInt(nodeIdString);
                String GCMId = db.getGCMId(user);
                nodeMap.get(nodeId).addNodeUser(GCMId, subGraph.get(nodeId).timeElapsed);
            }
            
            return builder;
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GraphHandler.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return null;
        }
    }
}
