package reso.examples.gobackn;

import reso.common.HardwareInterface;
import reso.common.Link;
import reso.common.Message;
import reso.common.MessageListener;

import java.util.Random;

/**
 * Created by sacha on 27/04/16.
 */
public class UnreliableLink<M extends Message>
        extends Link{

    private /*final*/ HardwareInterface<M> iface1, iface2;

    public UnreliableLink(HardwareInterface<M> iface1, HardwareInterface<M> iface2, double length, long bitRate) throws Exception {
        super(iface1, iface2, length, bitRate);
    }

    @Override
    public void receive(HardwareInterface iface, Message msg) throws Exception {
        Random rn = new Random();
        if(rn.nextDouble() <= 0.8f)
            super.receive(iface, msg);
    }
}
