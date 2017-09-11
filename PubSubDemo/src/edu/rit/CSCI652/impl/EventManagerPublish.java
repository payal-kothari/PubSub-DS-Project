package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import static edu.rit.CSCI652.impl.EventManagerAdvertise.objectInStream;

/**
 * Created by payalkothari on 9/11/17.
 */
public class EventManagerPublish extends Thread{

    static Socket socket;
    static EventManager em = null;
    public EventManagerPublish(Socket socket) {
        this.em = new EventManager();
        this.socket = socket;
    }

    public void run() {
        try{
            objectInStream = new ObjectInputStream(socket.getInputStream());
            Event article = (Event) objectInStream.readObject();                // event = article
            em.eventMap.put(article.id, article);
            System.out.println("Article" +  "'" + article.title +"'"+ " added under topic name - " + "'" + article.topic.name + "'");
            System.out.println();
        }catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
