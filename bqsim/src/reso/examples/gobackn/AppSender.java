package reso.examples.gobackn;

import reso.common.AbstractApplication;
import reso.ip.IPAddress;
import reso.ip.IPHost;
import reso.ip.IPLayer;

import java.util.ArrayList;

/**
 * Created by sacha on 26/04/16.
 */
public class AppSender
    extends AbstractApplication{

    private final IPLayer ip;
    private final IPAddress dst;
    private ArrayList<GoBackNMessage> msg;
    private int i=0;
    private GoBackNProtocolSender proto;

    public AppSender(IPHost host, IPAddress dst, ArrayList<GoBackNMessage> msg){
        super(host, "sender");
        this.dst = dst;
        ip = host.getIPLayer();
        this.msg = msg;
        this.proto = new GoBackNProtocolSender((IPHost) host);
    }

    public void start()
        throws Exception{
        ip.addListener(GoBackNProtocolSender.IP_PROTO_GOBACKN_SENDER, proto);
        proto.send(dst, msg.get(i));// protocol.send
        i++;
    }

    public void stop(){}
}
