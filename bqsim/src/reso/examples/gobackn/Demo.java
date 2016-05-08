package reso.examples.gobackn;

import reso.common.AbstractTimer;
import reso.common.Link;
import reso.common.Network;
import reso.common.Node;
import reso.ethernet.EthernetAddress;
import reso.ethernet.EthernetFrame;
import reso.ethernet.EthernetInterface;
import reso.ip.IPAddress;
import reso.ip.IPHost;
import reso.ip.IPRouter;
import reso.scheduler.AbstractScheduler;
import reso.scheduler.Scheduler;
import reso.utilities.NetworkBuilder;
import reso.utilities.NetworkGrapher;

import java.io.*;

/**
 * Created by sacha on 13/04/16.
 */
public class Demo {

    public static void main(String[] args){

        AbstractScheduler scheduler= new Scheduler();
        Network network= new Network(scheduler);
        try{
            final EthernetAddress MAC_ADDR1= EthernetAddress.getByAddress(0x00, 0x26, 0xbb, 0x4e, 0xfc, 0x28);
            final EthernetAddress MAC_ADDR2= EthernetAddress.getByAddress(0x00, 0x26, 0xbb, 0x4e, 0xfc, 0x29);
            final IPAddress IP_ADDR1= IPAddress.getByAddress(192, 168, 0, 1);
            final IPAddress IP_ADDR2= IPAddress.getByAddress(192, 168, 0, 2);

            Scenario scenar = new Scenario(10, 1);
            IPHost host1= NetworkBuilder.createHost(network, "H1", IP_ADDR1, MAC_ADDR1);
            host1.getIPLayer().addRoute(IP_ADDR2, "eth0");
            host1.addApplication(new AppSender(host1, IP_ADDR2, scenar.messages));


            IPHost host2= NetworkBuilder.createHost(network,"H2", IP_ADDR2, MAC_ADDR2);
            host2.getIPLayer().addRoute(IP_ADDR1, "eth0");
            host2.addApplication(new AppReceiver(host2));

            EthernetInterface h1_eth0= (EthernetInterface) host1.getInterfaceByName("eth0");
            EthernetInterface h2_eth0= (EthernetInterface) host2.getInterfaceByName("eth0");

            new UnreliableLink<EthernetFrame>(h1_eth0, h2_eth0, 5000000, 100000);

            AbstractTimer at = new AbstractTimer(scheduler, scenar.interval, true) {
                int i;
                @Override
                protected void run() throws Exception {
                    if(i < scenar.messages.size()) {
                        host1.start();
                        i++;
                    }
                    else
                        stop();
                }
            };

            File f= new File("/tmp/network.graphviz");
            Writer w= new BufferedWriter(new FileWriter(f));
            NetworkGrapher.toGraphviz(network ,new PrintWriter(w));
            w.close();

            at.start();
            host2.start();

            scheduler.run();

        }
        catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
