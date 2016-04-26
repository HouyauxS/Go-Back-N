package reso.examples.gobackn;

import reso.common.Message;

/**
 * Created by sacha on 25/04/16.
 */
public class GoBackNMessage implements Message{

    public final int num;
    public final boolean corrupted;

    public GoBackNMessage(boolean corrupted,int num){
        this.num = num;
        this.corrupted = corrupted;
    }

    public String toString() {
        return "Segment number : " + num + " and corrupted : " + corrupted;
    }

    @Override
    public int getByteLength() {
        return Integer.SIZE / 8;
    }
}
