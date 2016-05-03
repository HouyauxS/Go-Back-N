package reso.examples.gobackn;

import reso.ip.Datagram;
import reso.ip.IPAddress;
import reso.ip.IPHost;
import reso.ip.IPInterfaceAdapter;
import reso.ip.IPInterfaceListener;

/**
 * Created by sacha on 26/04/16.
 */
public class GoBackNProtocol implements IPInterfaceListener{

    public static final int IP_PROTO_GOBACKN = Datagram.allocateProtocolNumber("Go-Back-N");

    private final IPHost host;
    public GoBackNProtocol(IPHost host){this.host= host;}

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram)
        throws Exception{
        GoBackNMessage msg = (GoBackNMessage) datagram.getPayload();
        System.out.println("Go-Back-N message from "+datagram.src+" to "
        + datagram.dst +" : "+ msg);
        if(msg.num < 10)
            host.getIPLayer().send(IPAddress.ANY, datagram.src, IP_PROTO_GOBACKN, new GoBackNMessage(false, msg.num+1));
    }
}
