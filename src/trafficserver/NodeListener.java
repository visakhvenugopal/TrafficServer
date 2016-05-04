/*
 * Copyright (C) 2016 Visakh Venugopal
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package trafficserver;

/**
 *
 * @author Visakh Venugopal
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Node;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
//import org.json.

/**
 *
 * @author Visakh Venugopal
 */
public class NodeListener 
{

    NodeListener()
    {
        try { 
            new Thread(new readThread()).start();
        } catch (SocketException ex) {
            Logger.getLogger(NodeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}

class readThread implements Runnable
{
    String msg ;
    DatagramSocket readSocket;
    DatagramPacket inPacket ; 
    private byte[] recvBuff;
    int nodeId ;
    
    readThread() throws SocketException
    {
        
        
        recvBuff = new byte[1024];
        readSocket = new DatagramSocket(8000);
        inPacket = new DatagramPacket(recvBuff,recvBuff.length);
        
    }

    @Override
    public void run()
    {
       try
       {
           while(true)
           {
               readSocket.receive(inPacket);
               msg = new String(inPacket.getData(),0,inPacket.getLength());
               //System.out.println(msg);
               Object obj = JSONValue.parse(msg);
               JSONObject jsonObject = (JSONObject) obj; 
               
               String id ;
               double cindex;
               id = (String)jsonObject.get("id");
               cindex = (Double)jsonObject.get("cindex");
               
               Node node = Canvas.graph.getNode(id);
               node.setAttribute("ui.color",cindex);
               //System.out.println(id + ">> "+ cindex);
               
               
               
           }
           
       }
       catch(IOException e)
       {
           System.out.println("Node "+ nodeId + e);
           readSocket.close();
       }
    }
    
}
