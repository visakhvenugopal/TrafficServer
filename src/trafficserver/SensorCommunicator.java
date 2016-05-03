/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Acer-Lap
 */
public class SensorCommunicator extends Thread {
    
    private static final int PORT_NUM = 8001;
    DatagramSocket serverSocket;
    
    @Override
    public void run() {
        System.out.printf("LogServer running on port: %s%n", PORT_NUM);
        
        try {
            try {
                serverSocket = new DatagramSocket(PORT_NUM, InetAddress.getLocalHost());
            } catch (UnknownHostException ex) {
                Logger.getLogger(SensorCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SocketException ex) {
            Logger.getLogger(SensorCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            byte buffer[] = new byte[1024];
            DatagramPacket dgp = new DatagramPacket(buffer, buffer.length);
            System.out.println(serverSocket.toString());
            try {
                serverSocket.receive(dgp);
                new SensorRequestHandler(dgp).start();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            }
        }
    }
}

class SensorRequestHandler extends Thread {
    
    protected DatagramPacket data;
    
    public SensorRequestHandler(DatagramPacket receivedData) {
        super();
        this.data = receivedData;
    }
    
    @Override
    public void run() {
        String msg = new String(data.getData(), 0, data.getLength());
        JSONObject json = new JSONObject(msg);
        
        int nodeId = json.getInt("nodeid");
        int time = json.getInt("timestamp");
        int congestionIndex = json.getInt("index");
        
        RNode node = GraphHandler.nodeMap.get(nodeId);
        if (node != null) {
            if (time > node.getLastUpdateTime()) {
                switch (congestionIndex) {
                    case 0:
                        break;
                    case 1:
                        node.setNodeAvailability(true);
                        node.setCurationTime(json.getInt("ttc"));
                        node.informCongestion();
                        break;
                    case 2:;
                        node.setNodeAvailability(false);
                        node.setCurationTime(json.getInt("ttc"));
                        node.informCongestion();
                        break;
                }
            
                node.setCongestionIndex(congestionIndex);
                node.setLastUpdateTime(time);
            }
        }
    }
}
