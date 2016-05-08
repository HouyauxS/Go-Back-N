package reso.examples.gobackn;

import reso.ip.*;

/**
 * Created by sacha on 3/05/16.
 */
public class GoBackNProtocolReceiver implements IPInterfaceListener{

    public static final int IP_PROTO_GOBACKN_RECEIVER = Datagram.allocateProtocolNumber("Go-Back-N-S");
    int seq = 0; // the expected sequence number

    private final IPHost host;
    public GoBackNProtocolReceiver(IPHost host){this.host= host;}

    @Override
    /**
     * This method apply the pseudo code view in class for the Go-Back-N protocol
     */
    public void receive(IPInterfaceAdapter src, Datagram datagram)
            throws Exception {
        GoBackNMessage msg = (GoBackNMessage) datagram.getPayload();
        System.out.println("Receiver : Go-Back-N message from " + datagram.src + " to "
                + datagram.dst + " : " + msg);
        if (msg.num == seq) { // message received in order
            /*gérerl'application
            *
            * String data = msg.payload;
            * envoie de data à couche application
            * */
            seq++;
            host.getIPLayer().send(IPAddress.ANY, datagram.src, GoBackNProtocolSender.IP_PROTO_GOBACKN_SENDER, new ACKMessage(seq));
            System.out.println("Receiver : message in order, Ack sent");
        }
        else { //message not in order
            host.getIPLayer().send(IPAddress.ANY, datagram.src, GoBackNProtocolSender.IP_PROTO_GOBACKN_SENDER, new ACKMessage(seq-1));
            System.out.println("Receiver : message not in order, previous Ack sent");
        }
    }
}
