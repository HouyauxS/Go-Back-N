package reso.examples.gobackn;

import reso.common.Message;

/**
 * Created by sacha on 25/04/16.
 */
public class GoBackNMessage implements Message{

    public final int num;
    public final String payload;

    public GoBackNMessage(int num, String payload){
        this.num = num;
        this.payload = payload;
    }

    public GoBackNMessage(int num){
        this.num = num;
        payload= "";
    }

    public String toString() {
        return "Message number : " + num + " " + payload;
    }

    @Override
    public int getByteLength() {
        return Integer.SIZE / 8;
    }
}
