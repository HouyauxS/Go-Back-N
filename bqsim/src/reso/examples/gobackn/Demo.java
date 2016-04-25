package reso.examples.gobackn;

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
            final EthernetAddress MAC_ADDR3= EthernetAddress.getByAddress("00:25:bb:4e:fc:28");
            final EthernetAddress MAC_ADDR4= EthernetAddress.getByAddress("00:25:bb:4e:fc:29");
            final IPAddress IP_ADDR1= IPAddress.getByAddress(192, 168, 0, 1);
            final IPAddress IP_ADDR2= IPAddress.getByAddress(192, 168, 0, 2);
            final IPAddress IP_ADDR3= IPAddress.getByAddress(192, 160, 0, 1);
            final IPAddress IP_ADDR4= IPAddress.getByAddress(192, 160, 0, 2);

            final EthernetAddress[] ROUTER_MAC_ADDR = {MAC_ADDR3, MAC_ADDR4};
            final IPAddress[] ROUTER_IP_ADDR = {IP_ADDR3, IP_ADDR4};

            IPHost host1= NetworkBuilder.createHost(network, "H1", IP_ADDR1, MAC_ADDR1);
            host1.getIPLayer().addRoute(IP_ADDR3, "eth0");
            host1.getIPLayer().addRoute(IP_ADDR2, "eth0");
            //host1.addApplication(new AppSender(host1, IP_ADDR2, 5));

            IPRouter router1 = NetworkBuilder.createRouter(network,"R1",ROUTER_IP_ADDR,ROUTER_MAC_ADDR);
            router1.getIPLayer().addRoute(IP_ADDR1, "eth0");
            router1.getIPLayer().addRoute(IP_ADDR2, "eth1");

            IPHost host2= NetworkBuilder.createHost(network,"H2", IP_ADDR2, MAC_ADDR2);
            host2.getIPLayer().addRoute(IP_ADDR4, "eth0");
            host2.getIPLayer().addRoute(IP_ADDR1, "eth0");
            //host2.addApplication(new AppReceiver(host2));

            EthernetInterface h1_eth0= (EthernetInterface) host1.getInterfaceByName("eth0");
            EthernetInterface h2_eth0= (EthernetInterface) host2.getInterfaceByName("eth0");
            EthernetInterface r1_eth0= (EthernetInterface) router1.getInterfaceByName("eth0");
            EthernetInterface r1_eth1= (EthernetInterface) router1.getInterfaceByName("eth1");

            new Link<EthernetFrame>(h1_eth0, r1_eth0, 5000000, 100000);
            new Link<EthernetFrame>(r1_eth1, h2_eth0, 2000000, 50000);

            File f= new File("/tmp/network.graphviz");
            Writer w= new BufferedWriter(new FileWriter(f));
            NetworkGrapher.toGraphviz(network ,new PrintWriter(w));
            w.close();

        }
        catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
