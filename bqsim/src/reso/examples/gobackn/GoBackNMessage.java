package reso.examples.gobackn;

import reso.common.Message;

/**
 * Created by sacha on 25/04/16.
 */
public class GoBackNMessage implements Message{

    public final int num;

    public GoBackNMessage(int num){
        this.num = num;
    }

    public String toString() {
        return "Message number : " + num;
    }

    @Override
    public int getByteLength() {
        return Integer.SIZE / 8;
    }
}
