package edu.rit.CSCI652.impl;

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
                System.out.println("accepted a connection from  port :" + socket.getPort() );
                objectInStream = new ObjectInputStream(socket.getInputStream());
                List<String> l = (List<String>) objectInStream.readObject();

                System.out.println("New articles added under following topics : ");
                for(String name : l){
                    System.out.println("* " + name);
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
