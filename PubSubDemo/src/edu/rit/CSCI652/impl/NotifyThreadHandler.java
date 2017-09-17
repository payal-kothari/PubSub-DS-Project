package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by payalkothari on 9/16/17.
 */
public class NotifyThreadHandler extends Thread{
    private static List<Event> eventList;
    private static InetAddress ip;


    public NotifyThreadHandler(InetAddress ipAddress, List<Event> events) {
        this.eventList = events;
        this.ip = ipAddress;
    }


    public void run(){
        Iterator iter = eventList.iterator();
        Socket s = null;
        try {
            s = new Socket(ip, 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //List<String> l = new ArrayList<>();
        List<Event> l = new ArrayList<>();

        while (iter.hasNext()){
            Event e = (Event) iter.next();
//            l.add(e.getTopic().getName());
            l.add(e);
        }

        try {
            ObjectOutputStream outS = new ObjectOutputStream(s.getOutputStream());
            outS.writeObject(l);
            outS.flush();
            outS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            EventManager.getSubscribersToContact().remove(ip);
        }
    }
}
