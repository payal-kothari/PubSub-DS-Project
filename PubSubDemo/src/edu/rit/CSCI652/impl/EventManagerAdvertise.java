package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Topic;


import java.io.*;
import java.net.Socket;

/**
 * Created by payalkothari on 9/11/17.
 */


public class EventManagerAdvertise extends Thread {

    static Socket socket;
    static EventManager em = null;
    public static ObjectInputStream objectInStream = null;

    public EventManagerAdvertise(Socket socket) {
        this.em = new EventManager();
        this.socket = socket;
    }

    public void run() {
        try{

            objectInStream = new ObjectInputStream(socket.getInputStream());
            Topic topic = (Topic) objectInStream.readObject();
            em.addTopic(topic);

        }catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
