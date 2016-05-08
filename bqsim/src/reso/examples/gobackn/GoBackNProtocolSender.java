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
    private int next_seq_num = 0;
    protected int win_size = 4;
    protected int ssthresh = 16;
    private AbstractTimer at;
    private IPAddress dst;
    private ArrayList<GoBackNMessage> buffer;
    protected ArrayList<Integer> duplicate;//used to see if we receive 3 duplicate ack

    public GoBackNProtocolSender(IPHost host) {
        this.host = host;
        this.buffer = new ArrayList<GoBackNMessage>(1000);
        /*
        * Creation of a scheduler and timer for the Go-Back-N timeout part,
        * The timer occurs after 3 seconds if there's no ack received
        * It sends again all the message in the window
        */
        this.at = new AbstractTimer(host.getNetwork().getScheduler(), 3, false) {
            @Override
            protected void run() throws Exception {
                for (int i = send_base; i < next_seq_num; i++) {
                    host.getIPLayer().send(IPAddress.ANY, dst,
                            GoBackNProtocolReceiver.IP_PROTO_GOBACKN_RECEIVER, buffer.get(i));
                }
                ssthresh = win_size/2; //the timout congestion phase is apply here
                win_size = 1;
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
        //System.out.println("Sender : Go-Back-N message from " + datagram.src + " to " + datagram.dst + " : " + msg);

        // Receive part of Go-Back-N protocol
        System.out.println("next_seq_num = " + next_seq_num + " send_base = " + send_base);
        send_base = msg.num + 1;
        if (send_base == next_seq_num)
            at.stop();
        else
            at.start();
        deliverMsg(buffer.get(next_seq_num));
        congestionControl(msg);
    }

    /**
     * this method is used to apply the congestion control ( like tcp reno )
     * @param msg : ack message received by the Sender.
     */
    private void congestionControl(ACKMessage msg) { //the timeout phase is tested in the constructor
        if ( win_size < ssthresh ) // in this case , the window grows exponentially
            win_size*=2;
        else if (win_size >= ssthresh) // in this case , the window grows linearly
            win_size+=1;
        else if (duplicate.size() == 3) { //in this case, we have 3 same ack, we apply the curse method
            ssthresh = win_size/2;
            win_size = ssthresh;
            duplicate.clear();
        }
        else if (msg.num==duplicate.get(0)) { //we verify if we have a similar ack than the previous
            duplicate.add(new Integer(msg.num));
        }
        else //we initialize the ack to compare, to see if we have the same ack
            duplicate.clear();
            duplicate.add(msg.num);
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
