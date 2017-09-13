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

    public ThreadHandler(Socket socket) {
        this.em = new EventManager();
        this.socket = socket;
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
                objectInStream = new ObjectInputStream(socket.getInputStream());
                Event article = (Event) objectInStream.readObject();                // event = article
                em.eventMap.put(article.id, article);
                System.out.println("Article" +  "'" + article.title +"'"+ " added under topic name - " + "'" + article.topic.name + "'");
                System.out.println();
            }

           // objectInStream.close();
        }catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
