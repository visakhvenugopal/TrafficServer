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

    private Map<Integer, SubNodes> map;//hash map to store sub graph

    //Subnodes are arranged according to its heuristic value
    private SubNodes priorityList;
    private int user;

    //indicates the positions of the currently processing node in the likedlist    
    private static final int SINGLE_ELEMENT = 0;
    private static final int LEFT_END = 1;
    private static final int RIGHT_END = 2;
    private static final int MIDDLE = 3;
    private static final int NOT_IN_LIST = 4;

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
    public Message.Builder getRout(int node1, int node2){//double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        try {
            int[] nearestNodes = new int[2];

            //nearestNodes = db.getNearestNodes(startLatitude, startLongitude, endLatitude, endLongitude);
            nearestNodes[0] = node1;
            nearestNodes[1] = node2;
            System.out.println("Rout for" + nearestNodes[0] + " &" + nearestNodes[1]);
            populateNodeMap(nearestNodes[0], nearestNodes[1]);

            return returnRout(nearestNodes[0], nearestNodes[1]);
        } catch (IOException ex) {
            Logger.getLogger(GraphHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //method that populates the edgeset to be travelled through
    private void populateNodeMap(int start, int end) {
        map = new HashMap<>();
        RNode node, eNode;

        if (start == end) {
            node = GraphHandler.nodeMap.get(end);
            map.put(end, new SubNodes(end, node.getLat(), node.getLon(), node.getEdgeList()));
            return;
        }

        REdge link;
        int linkId;

        SubNodes parent, child, dest;
        long currentBest = Integer.MAX_VALUE;

        node = GraphHandler.nodeMap.get(start);
        eNode = GraphHandler.nodeMap.get(end);

        int tempChildId;
        long tempTotalTimeEstimation;
        long tempTimeElapsed;
        double tempDT;
        double currSpeed;

        parent = new SubNodes(start, node.getLat(), node.getLon(), node.getEdgeList());
        dest = new SubNodes(end, eNode.getLat(), eNode.getLon(), eNode.getEdgeList());

        parent.dt = 0;
        parent.g = 0;
        parent.inPriorityList = true;

        map.put(start, parent);
        priorityList = parent;

        long currentSystemTime = (System.currentTimeMillis() / 60000);// % (60 * 24);

        while (priorityList != null) {
            SubNodes nd = priorityList;
            do {
                //System.out.print(nd.id + " ");
                nd = nd.next;
            } while (nd != null);
            parent = priorityList;
            //System.out.println("Current Node = " + parent.id);
            //process the edges on byone
            if (parent.id == end) {
                priorityList = parent.next;
                parent.next.prev = null;
                parent.next = parent.prev = null;
                parent.inPriorityList = false;
                continue;
            }
            for (String linkIdString : parent.outEdges) {
                linkId = Integer.parseInt(linkIdString);
                link = GraphHandler.edgeMap.get(linkId);
                tempChildId = link.getNodeOnTheOtherSide(parent.id);
                //System.out.println("        Current Link id = " + link.id + " -> " + tempChildId + " Availability = " + link.isAvailable());

                if (parent.parent != null && tempChildId == parent.parent.id) {
                    continue;
                }

                if (link.isAvailable()) {
                    if (tempChildId == end || link.isNodeAvailable(tempChildId)) {
                        if (map.containsKey(tempChildId)) {
                            //System.out.println("Inside mapContainsKey..");
                            child = map.get(tempChildId);
                            tempDT = parent.dt + link.getLength();
                            //System.out.println("TempDT = " + tempDT);
                            tempTimeElapsed = parent.g + link.getTravelTimeTo(tempChildId, (int) (currentSystemTime + parent.g));
                            currSpeed = tempDT / tempTimeElapsed;
                            tempTotalTimeEstimation = tempTimeElapsed + ((long) (distanceInKMeters(child.lat, child.lon, dest.lat, dest.lon) / currSpeed));
                            //System.out.println("comparison with " + tempTotalTimeEstimation + " and " + child.f);

                            if (tempTotalTimeEstimation < child.f) {
                               // System.out.println("Inside mapContainsKey&nodeUpdate");
                                child.dt = tempDT;
                                child.g = tempTimeElapsed;
                                child.f = tempTotalTimeEstimation;
                                child.parent = parent;

                                //the node must be replaced in the linked list
                                //first, it must be taken out of the list
                                switch (getNodePositionInTheList(child)) {
                                    case MIDDLE:
                                        child.next.prev = child.prev;
                                        child.prev.next = child.next;
                                        child.inPriorityList = false;
                                        break;
                                    case RIGHT_END:
                                        child.prev.next = null;
                                        child.inPriorityList = false;
                                        break;
                                    case LEFT_END:
                                        priorityList = child.next;
                                        child.next.prev = null;
                                        child.inPriorityList = false;
                                        break;
                                    case SINGLE_ELEMENT:
                                        priorityList = null;
                                        child.inPriorityList = false;
                                        break;
                                    default:
                                        break;
                                }

                                child.next = child.prev = null;
                                child.inPriorityList = false;
                                //add the child to the subGraph if the total time estimation is good
                                if (child.g < currentBest) {
                                    addNodeToPriorityList(child);
                                } else {
                                    //System.out.println("Node not added to prioriyList..");
                                }
                            }

                        } else {
                            //System.out.println("Inside new node prosesor");
                            node = GraphHandler.nodeMap.get(tempChildId);
                            child = new SubNodes(tempChildId, node.getLat(), node.getLon(), node.getEdgeList());
                            child.dt = parent.dt + link.getLength();
                            //System.out.println("TempDT = " + child.dt);
                            child.g = parent.g + link.getTravelTimeTo(tempChildId, (int) (currentSystemTime + parent.g));
                            child.f(dest);
                            //System.out.println("new f = " + child.f);
                            child.parent = parent;

                            //add the child to the subGraph if the otal time estimation is good
                            if (child.g < currentBest) {
                                map.put(tempChildId, child);
                                addNodeToPriorityList(child);
                            } else {
                                //System.out.println("Node not added to prioriyList..");
                            }
                        }
                        //update the current best time if the node is the destination
                        if (tempChildId == end) {
                            if (child.g < currentBest) {
                                currentBest = child.g;

                                //remove all nodes with greater estimation from the list
                                SubNodes iterator = priorityList;
                                int totalcount = 0, deleted = 0;
                                while (iterator != null) {
                                    totalcount++;
                                    if (iterator.g > currentBest) {
                                        deleted++;
                                        switch (getNodePositionInTheList(iterator)) {
                                            case MIDDLE:
                                                iterator.next.prev = iterator.prev;
                                                iterator.prev.next = iterator.next;
                                                iterator.inPriorityList = false;
                                                break;
                                            case RIGHT_END:
                                                iterator.prev.next = null;
                                                iterator.inPriorityList = false;
                                                break;
                                            case LEFT_END:
                                                priorityList = iterator.next;
                                                iterator.next.prev = null;
                                                iterator.inPriorityList = false;
                                                break;
                                            case SINGLE_ELEMENT:
                                                priorityList = null;
                                                iterator.inPriorityList = false;
                                                break;
                                            default:
                                                iterator.next = parent.prev = null;
                                                iterator.inPriorityList = false;
                                                break;
                                        }
                                    }

                                    iterator = iterator.next;
                                }
                                //System.out.println("******************");
                                //System.out.println("Total count was : " + totalcount + "| Deleted " + deleted);
                                //System.out.println("******************");
                            }
                        }
                    } else {
                        //System.out.println("tempChildId == end || link.isNodeAvailable(tempChildId) test failed");
                        //System.out.println("tempChildId == end = " + (tempChildId == end));
                        //System.out.println("link.isNodeAvailable(tempChildId) = " + link.isNodeAvailable(tempChildId));
                    }
                }
            }

            //remove current node from the list
            switch (getNodePositionInTheList(parent)) {
                case MIDDLE:
                    parent.next.prev = parent.prev;
                    parent.prev.next = parent.next;
                    break;
                case RIGHT_END:
                    parent.prev.next = null;
                    break;
                case LEFT_END:
                    priorityList = parent.next;
                    parent.next.prev = null;
                    break;
                case SINGLE_ELEMENT:
                    priorityList = null;
                    break;
                default:
                    break;
            }
            parent.next = parent.prev = null;
            parent.inPriorityList = false;
        }
    }

    private double distanceInKMeters(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;// * 1000;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private int getNodePositionInTheList(SubNodes node) {
        if (!node.inPriorityList) {
            return NOT_IN_LIST;
        }

        if (node.next == null) {
            if (node.prev == null) {//only one element
                return SINGLE_ELEMENT;
            } else {//left end
                return RIGHT_END;
            }
        } else if (node.prev == null) {//right end
            return LEFT_END;
        }

        return MIDDLE;
    }

    private void addNodeToPriorityList(SubNodes node) {
        //add node to the list
        //find the position to be added to
        SubNodes prevNode = null;
        SubNodes iterator = priorityList;
        while (iterator != null) {
            if (iterator.f > node.f) {
                break;
            }
            prevNode = iterator;
            iterator = iterator.next;
        }
        if (iterator == null) {
            if (prevNode == null) {
                priorityList = node;
                node.prev = node.next = null;
            } else {
                prevNode.next = node;
                node.prev = prevNode;
                node.next = null;
            }
        } else {
            if (prevNode == null) {
                node.next = iterator;
                node.prev = null;
                iterator.prev = node;
                priorityList = node;
            } else {
                node.prev = prevNode;
                prevNode.next = node;
                node.next = iterator;
                iterator.prev = node;

            }
        }
        node.inPriorityList = true;

    }

    private class SubNodes {

        int id;
        public double lat;
        public double lon;

        public double dt;
        public long g;

        public long h;
        public long f;

        public boolean inPriorityList;

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

        public long f(SubNodes endNode) {
            double speed = dt / g;
            double distToEnd = distanceInKMeters(lat, lon, endNode.lat, endNode.lon);
            h = (long) (distToEnd / speed);
            f = g + h;
            return f;
        }
    }

    private Message.Builder returnRout(int startNode, int endNode) throws IOException {
        try {
            //System.out.println("Inside returnRout..");
            int nodeId = endNode;
            int nodeCount = 0;
            SubNodes currNode = map.get(endNode);

            String nodeList = "";
            //create message
            Message.Builder builder = new Message.Builder();
            while (nodeId != startNode) {

                nodeCount++;
                builder.addData("" + nodeCount, nodeMap.get(nodeId).getNodeName());
                //builder.addData("ar" + nodeCount, "" + currNode.g);
                //builder.addData("la" + nodeCount, "" + currNode.lat);
                //builder.addData("lo" + nodeCount, "" + currNode.lon);

                nodeList = nodeList + nodeId + " ";//separated by space

                if (currNode != null) {
                    currNode = currNode.parent;
                } else {
                    System.out.println("Tree brocken..");
                    return null;
                }
                nodeId = currNode.id;

                //if(currNode != null)
            }

            nodeCount++;
            builder.addData("" + nodeCount, nodeMap.get(startNode).getNodeName());
            //builder.addData("a" + nodeCount, "" + currNode.g);
            //builder.addData("la" + nodeCount, "" + currNode.lat);
            //builder.addData("lo" + nodeCount, "" + currNode.lon);
            builder.addData("count", "" + nodeCount);

            nodeList = nodeList + startNode;

            if (!db.updateUserNodeList(user, nodeList)) {
                System.out.println("User updation failed");
                return null;
            }

            //add user to nodelist
            String GCMId = db.getGCMId(user);
            for (String nodeIdString : nodeList.split(" ")) {
                nodeId = Integer.parseInt(nodeIdString);
                nodeMap.get(nodeId).addNodeUser(GCMId, map.get(nodeId).g);
            }

            return builder;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GraphHandler.class
                    .getName()).log(Level.SEVERE, null, ex);

            return null;
        }

    }

    public String check() {
        return "Graph OK.";
    }
}
