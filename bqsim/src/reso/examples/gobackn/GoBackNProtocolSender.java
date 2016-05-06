package reso.examples.gobackn;

import reso.common.AbstractTimer;
import reso.ip.*;

import java.util.ArrayList;

/**
 * Created by sacha on 26/04/16.
 */
public class GoBackNProtocolSender implements IPInterfaceListener {

    public static final int IP_PROTO_GOBACKN_SENDER = Datagram.allocateProtocolNumber("Go-Back-N-S");

    private final IPHost host;
    private int send_base = 0;
    private int next_seq_num = 1;
    protected int win_size = 4;
    private AbstractTimer at;
    private IPAddress dst;
    private ArrayList<GoBackNMessage> buffer;

    public GoBackNProtocolSender(IPHost host) {
        this.host = host;
        /*
        * Creation of a scheduler and timer for the Go-Back-N timeout part,
        * The timer occurs after 3 seconds if there's no ack received
        * It sends again all the message in the window
        */
        this.at = new AbstractTimer(host.getNetwork().getScheduler(), 3, false) {
            @Override
            protected void run() throws Exception {
                for (int i = send_base; i < next_seq_num - 1; i++) {
                    host.getIPLayer().send(IPAddress.ANY, dst,
                            GoBackNProtocolReceiver.IP_PROTO_GOBACKN_RECEIVER, buffer.get(i));
                }
                start();
            }
        };
    }

    /*
    *   Receive ACK from the receiver
    * */
    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram)
            throws Exception {
        ACKMessage msg = (ACKMessage) datagram.getPayload();
        // Print the IP source and IP dest plus the content of the message
        System.out.println("Sender : Go-Back-N message from " + datagram.src + " to " + datagram.dst + " : " + msg);

        // Receive part of Go-Back-N protocol
        send_base = msg.num + 1;
        if (send_base == next_seq_num)
            at.stop();
        else
            at.start();
        deliverMsg(buffer.get(next_seq_num));
    }

    /*
    *   This method will store the message in a buffer to deliver it later
    * */
    public void send(IPAddress dst, GoBackNMessage msg)
        throws Exception{
        this.dst = dst;
        buffer.add(msg);
        deliverMsg(buffer.get(next_seq_num));
    }


    /*
    *   Deliver the message to the receiver
    * */
    public void deliverMsg(GoBackNMessage msg)
        throws Exception{
        // Send part of Go-Back-N protocol
        if (next_seq_num < send_base + win_size) {
            host.getIPLayer().send(IPAddress.ANY, dst, GoBackNProtocolReceiver.IP_PROTO_GOBACKN_RECEIVER, msg);
            if (send_base == next_seq_num)
                at.start();
            next_seq_num++;
        }
    }
}
