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
import java.util.Map;

/**
 * Created by payalkothari on 9/16/17.
 */
public class NotifyThreadHandler extends Thread{
    private static List<Event> eventList;
    private static InetAddress ip;

    public void run(){
        while(true){
            Iterator iter = EventManager.getSubscribersToContact().entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry pair = (Map.Entry)iter.next();
                ip = (InetAddress) pair.getKey();
                eventList = (List<Event>) pair.getValue();
                eventList.iterator();
                try(Socket s = new Socket(ip, 8000)) {
                    if(s.isConnected()){
                        List<Event> l = new ArrayList<>();
                        Iterator it = eventList.iterator();
                        while (it.hasNext()){
                            Event e = (Event) it.next();
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

                        EventManager.getSubscribersToContact().remove(ip);
                    }
                } catch (IOException e) {

                }

            }


        }
    }
}
