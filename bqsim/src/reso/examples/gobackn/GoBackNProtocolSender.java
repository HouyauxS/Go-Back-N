package reso.examples.gobackn;

import reso.ip.Datagram;
import reso.ip.IPAddress;
import reso.ip.IPHost;
import reso.ip.IPInterfaceAdapter;
import reso.ip.IPInterfaceListener;

/**
 * Created by sacha on 26/04/16.
 */
public class GoBackNProtocolSender implements IPInterfaceListener{

    public static final int IP_PROTO_GOBACKN_SENDER = Datagram.allocateProtocolNumber("Go-Back-N-S");

    private final IPHost host;
    public GoBackNProtocolSender(IPHost host){this.host= host;}

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram)
        throws Exception{
        ACKMessage msg = (ACKMessage) datagram.getPayload();
        System.out.println("Sender : Go-Back-N message from "+datagram.src+" to "
        + datagram.dst +" : "+ msg);
        if(msg.num > 0)
            host.getIPLayer().send(IPAddress.ANY, datagram.src, GoBackNProtocolReceiver.IP_PROTO_GOBACKN_RECEIVER, new GoBackNMessage(msg.num, "My message"));
    }
}
