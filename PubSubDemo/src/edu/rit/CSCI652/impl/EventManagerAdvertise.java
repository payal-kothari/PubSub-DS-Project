package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;
import edu.rit.CSCI652.impl.EventManager;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import static edu.rit.CSCI652.impl.EventManager.eventMap;

/**
 * Created by payalkothari on 9/11/17.
 */


public class EventManagerServer extends Thread   {

    static Socket socket;
    static EventManager em = null;
    public static ObjectInputStream objectInStream = null;
    public EventManagerServer(Socket socket) {
        this.em = new EventManager();
        this.socket = socket;
    }

    public void run() {
        try{

            objectInStream = new ObjectInputStream(socket.getInputStream());
            System.out.println(" sec inside input");
            Topic topic = (Topic) objectInStream.readObject();
            System.out.println("adding topic " + topic);
           // em.addTopic(topic);
//            if(input.equals("Advertise")){
////                objectInStream = new ObjectInputStream(socket.getInputStream());
////                System.out.println(input + " sec inside input");
////                Topic topic = (Topic) objectInStream.readObject();
////                System.out.println("adding topic " + topic);
////                em.addTopic(topic);
//            }else if(input.equals("Publish")){
//                ObjectInputStream objectInStream = new ObjectInputStream(socket.getInputStream());
//                Event article = (Event) objectInStream.readObject();                // event = article
//                em.eventMap.put(article.id, article);
//                System.out.println("Article added under topic " + article.topic.name);
//            }
        }catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
