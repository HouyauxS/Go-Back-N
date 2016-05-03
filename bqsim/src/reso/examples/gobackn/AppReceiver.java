package reso.examples.gobackn;

import reso.common.AbstractApplication;
import reso.ip.IPHost;
import reso.ip.IPLayer;

/**
 * Created by sacha on 26/04/16.
 */
public class AppReceiver
    extends AbstractApplication{

    private  final IPLayer ip;

    public AppReceiver(IPHost host) {
        super(host, "receiver");
        ip= host.getIPLayer();
    }

    public void start(){

        ip.addListener(GoBackNProtocolReceiver.IP_PROTO_GOBACKN_RECEIVER, new GoBackNProtocolReceiver((IPHost) host));
    }

    public void stop(){}
}
