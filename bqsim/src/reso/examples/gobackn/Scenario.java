package reso.examples.gobackn;

import java.util.ArrayList;

/**
 * Created by sacha on 6/05/16.
 */
public class Scenario {

    public final ArrayList<GoBackNMessage> messages;
    public final double interval;

    public Scenario(int numberOfMessages, double interval) {
        this.interval = interval;
        messages = new ArrayList<GoBackNMessage>();
        for(int i=0; i < numberOfMessages;i++){
            messages.add(new GoBackNMessage(i, "I'm the message number : "+i));
        }
    }

}
