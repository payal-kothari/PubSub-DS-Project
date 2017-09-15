package edu.rit.CSCI652.impl;

import java.net.InetAddress;

/**
 * Created by payalkothari on 9/15/17.
 */
public class SubscriberDetails {

    InetAddress ipAddress;
    int port;

    public SubscriberDetails(InetAddress ip, int port){
        this.ipAddress = ip;
        this.port = port;
    }
}
