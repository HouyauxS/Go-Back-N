package reso.examples.gobackn;

import reso.ip.*;

/**
 * Created by sacha on 3/05/16.
 */
public class GoBackNProtocolReceiver implements IPInterfaceListener{

    public static final int IP_PROTO_GOBACKN_RECEIVER = Datagram.allocateProtocolNumber("Go-Back-N-S");

    private final IPHost host;
    public GoBackNProtocolReceiver(IPHost host){this.host= host;}

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram)
            throws Exception{
        GoBackNMessage msg = (GoBackNMessage) datagram.getPayload();
        System.out.println("Receiver : Go-Back-N message from "+datagram.src+" to "
                + datagram.dst +" : "+ msg);
        if(msg.num > 0)
            host.getIPLayer().send(IPAddress.ANY, datagram.src, GoBackNProtocolSender.IP_PROTO_GOBACKN_SENDER, new ACKMessage(msg.num-1));
    }
}
