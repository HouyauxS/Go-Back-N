package reso.examples.gobackn;

import reso.common.AbstractApplication;
import reso.ip.IPAddress;
import reso.ip.IPHost;
import reso.ip.IPLayer;

/**
 * Created by sacha on 26/04/16.
 */
public class AppSender
    extends AbstractApplication{

    private final IPLayer ip;
    private final IPAddress dst;

    public AppSender(IPHost host, IPAddress dst){
        super(host, "sender");
        this.dst = dst;
        ip = host.getIPLayer();
    }

    public void start()
        throws Exception{
        ip.addListener(GoBackNProtocolSender.IP_PROTO_GOBACKN_SENDER, new GoBackNProtocolSender((IPHost) host));
        ip.send(IPAddress.ANY, dst, GoBackNProtocolReceiver.IP_PROTO_GOBACKN_RECEIVER, new GoBackNMessage(5));
    }

    public void stop(){}
}
