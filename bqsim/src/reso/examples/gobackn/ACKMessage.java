package reso.examples.gobackn;

import reso.common.Message;

/**
 * Created by sacha on 3/05/16.
 */
public class ACKMessage implements Message {

    public final int num;

    public ACKMessage(int num){
        this.num = num;
    }

    public String toString() {
        return "ACK number : " + num;
    }

    @Override
    public int getByteLength() {
        return Integer.SIZE / 8;
    }
}
