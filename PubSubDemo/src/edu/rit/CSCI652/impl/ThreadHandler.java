package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;


import java.io.*;
import java.net.Socket;

/**
 * Created by payalkothari on 9/11/17.
 */


public class ThreadHandler extends Thread {

    static Socket socket;
    static EventManager em = null;
    public static ObjectInputStream objectInStream = null;
    private static int currentPort;

    public ThreadHandler(Socket socket, int port) {
        this.em = new EventManager();
        this.socket = socket;
        this.currentPort = port;
    }

    public void run() {
        try{
            ObjectInputStream objectInStream = new ObjectInputStream(socket.getInputStream());
            String input = objectInStream.readUTF();
            if(input.equals("Advertise")){
                System.out.println("Advertising a topic");
                Topic topic = (Topic) objectInStream.readObject();
                em.addTopic(topic);
            }else if(input.equals("Publish")){
                System.out.println("Publishing an article");
                Event article = (Event) objectInStream.readObject();                // event = article
                EventManager.eventMap.put(article.id, article);
                System.out.println("Article " +  "'" + article.title +"'"+ " added under topic name - " + "'" + article.topic.name + "'");
            }
        }catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        finally {
            try {
                socket.close();
                System.out.println("Connection on " + socket.getLocalPort() + " closed : " + socket.isClosed()  );
                System.out.println();
                EventManager.busyPorts.remove(currentPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
