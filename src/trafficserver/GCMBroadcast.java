/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficserver;

/**
 *
 * @author Acer-Lap
 */
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import java.util.List;

public class GCMBroadcast {

    private final String SENDER_ID = "AIzaSyDAKpEp5EXpFpseu_i3_swwsLPUPtXaoLM";
    private Sender sender;//Object that seends the message

    public GCMBroadcast() {

    }

    //method to broadcast
    public void broadcast(List<String> androidTargets, String collapseKey, Message message) {
        /*try {
            // This Message object will hold the data that is being transmitted
            // to the Android client devices.
            sender = new Sender(SENDER_ID);

            MulticastResult result = sender.send(message, androidTargets, 10);

            if (result.getResults() != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {
                }
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }

        } catch (IOException ex) {
            Logger.getLogger(GCMBroadcast.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        System.out.println(message.toString());
    }

}
