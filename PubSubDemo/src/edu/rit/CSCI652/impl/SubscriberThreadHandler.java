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
                Socket socket = SubscriberNode.getNotificationListenSocket().accept();
                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\taccepted a connection from  port :" + socket.getPort() );
                objectInStream = new ObjectInputStream(socket.getInputStream());
                //List<String> l = (List<String>) objectInStream.readObject();
                List<Event> l = (List<Event>) objectInStream.readObject();

                System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNew articles ");
                for(Event event : l){
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* Topic name - " + event.getTopic().getName() + " * Article name - " + event.getTitle());
                }

                socket.close();
                objectInStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

}
