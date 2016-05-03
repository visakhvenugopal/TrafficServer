/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

import com.google.android.gcm.server.Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Acer-Lap
 */
public class ClientCommunicator extends Thread {

    private static int PORT_NUM = 8000;
    DatagramSocket serverSocket;

    public ClientCommunicator() {
    }

    @Override
    public void run() {
        System.out.printf("LogServer running on port: %s%n", PORT_NUM);
        try {
            serverSocket = new DatagramSocket(PORT_NUM, InetAddress.getLocalHost());
        } catch (SocketException ex) {
            Logger.getLogger(ClientCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) 
        {
            PORT_NUM = 8000;
            if (serverSocket.isBound()) {
                System.out.println("Intialised sever Socket :" + serverSocket.getPort());
            } else {
                System.out.println("Cuildn't Intialise sever Socket");
            }
            byte buffer[] = new byte[1024];
            DatagramPacket dgp = new DatagramPacket(buffer, buffer.length);
           
            System.out.println(serverSocket.getLocalAddress() + "" + serverSocket.getLocalPort());
            try {
                serverSocket.receive(dgp);
                String msg = new String(dgp.getData(), 0, dgp.getLength());
                System.out.println(msg);
                ClientRequestHandler crh =new ClientRequestHandler(msg);
                crh.start();
            } catch (IOException exception) {
                exception.printStackTrace();
            } 
            }
        }
    }


class ClientRequestHandler extends Thread {

    protected DatagramPacket data;
    private DBHandler db;
    String msg = null;
/*
    public ClientRequestHandler(DatagramPacket receivedData) {
        this.data = receivedData;
        db = new DBHandler();
    }
*/
    public ClientRequestHandler(String msg) {
        this.msg = msg ;
        db = new DBHandler();
    }
    
    @Override
    public void run() {
        //String msg = new String(data.getData(), 0, data.getLength());
        
        JSONObject json = new JSONObject(msg);
        String requestType = json.getString("rt");

        if (requestType.equals("reg")) {//new user registration
            String GCM = json.getString("gcm");
            int newUserId = db.addNewUser(GCM);

            Message.Builder builder = new Message.Builder();
            builder.addData("key", "reg");
            builder.addData("userid", "" + newUserId);
            Message gcmMsg = builder.build();

            List<String> androidTargetList = new ArrayList<>();
            String collapseKey = "registration";
            androidTargetList.add(GCM);

            new GCMBroadcast().broadcast(androidTargetList, collapseKey, gcmMsg);
        } else if (requestType.equals("rout")) {
            GraphHandler graph = new GraphHandler(json.getInt("userid"));
            Message.Builder builder = graph.getRout(json.getDouble("slat"), json.getDouble("slon"), json.getDouble("elat"), json.getDouble("elon"));

            if (builder == null) 
            {
                System.out.println("Request parse erro..!");
                return;
            }

            builder.addData("key", "rout");
            String collapseKey = "rout";

            String GCM = db.getGCMId(json.getInt("userid"));

            List<String> androidTargetList = new ArrayList<>();
            androidTargetList.add(GCM);

            new GCMBroadcast().broadcast(androidTargetList, collapseKey, builder.build());
        } else if (requestType.equals("stop")) {
            try {
                db.updateUserNodeList(json.getInt("userid"), "0");
            } catch (IOException ex) {
                Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
