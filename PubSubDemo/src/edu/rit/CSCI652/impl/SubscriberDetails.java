package edu.rit.CSCI652.impl;

import java.net.InetAddress;

/**
 * Created by payalkothari on 9/15/17.
 */
public class SubscriberDetails {
    private InetAddress ipAddress;
    private int port;

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }



    public SubscriberDetails(InetAddress ip, int port){
        this.ipAddress = ip;
        this.port = port;
    }
}
