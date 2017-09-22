package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by payalkothari on 9/16/17.
 */
public class SubscriberThreadHandler extends Thread{

    private static ObjectInputStream objectInStream;

    public void run(){
            try {
                while (true){
                    Socket socket = SubscriberNode.getNotificationListenSocket().accept();
                    objectInStream = new ObjectInputStream(socket.getInputStream());
                    //List<String> l = (List<String>) objectInStream.readObject();
                    List<Event> l = (List<Event>) objectInStream.readObject();

                    System.out.println("\n\n\n");
                    System.out.println("*************************** New articles ****************************");
                    for(Event event : l){
                        System.out.println("*  Topic name - " + event.getTopic().getName() + ", Article name - " + event.getTitle() );
                    }

                    System.out.println("*********************************************************************");
                    objectInStream.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

}
