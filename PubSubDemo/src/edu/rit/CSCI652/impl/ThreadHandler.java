package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Subscriber;
import edu.rit.CSCI652.demo.Topic;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by payalkothari on 9/11/17.
 */


public class ThreadHandler extends Thread implements Serializable{

    private static Socket socket;
    private static EventManager em = null;
    private static int currentPort;
    private static EventManager threadSyncObject;


    public ThreadHandler(Socket socket, int port,EventManager threadSyncObject) {
        this.em = new EventManager();
        this.socket = socket;
        this.currentPort = port;
        this.threadSyncObject = threadSyncObject;
    }

    public void run() {
        synchronized (threadSyncObject){
        try {
            ObjectInputStream objectInStream = new ObjectInputStream(socket.getInputStream());
            String input = objectInStream.readUTF();
            if (input.equals("Advertise")) {
                Topic topic = (Topic) objectInStream.readObject();
                int index = em.getTopidIndex();
                em.setTopidIndex(++index);
                topic.setId(em.getTopidIndex());
                em.addTopic(topic);
            } else if (input.equals("Publish")) {

                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(EventManager.getTopicList());
                outObject.flush();
                int eventId = objectInStream.read();
                int topicId = objectInStream.read();
                String eventTitle = objectInStream.readUTF();
                String eventContent = objectInStream.readUTF();

                Topic topic = null;

                for (Topic t : EventManager.getTopicList()) {
                    if (t.getId() == topicId) {
                        topic = t;
                    }
                }

                Event article = new Event(eventId, topic, eventTitle, eventContent);
                EventManager.getEventMap().put(article.getId(), article);
                System.out.println("Article " + "'" + article.getTitle() + "'" + " published under topic name - " + "'" + article.getTopic().getName() + "'");
                List<SubscriberDetails> list = EventManager.getSubscriberMap().get(article.getTopic());

                if (list != null) {
                    addAddresses(list, article);
                }

            } else if (input.equals("Subscriber")) {
                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(EventManager.getTopicList());
                outObject.flush();

                if (!EventManager.getTopicList().isEmpty()) {
                    int topicIdToSubscribe = objectInStream.read();
                    System.out.println("Subscribed to topic - '" + EventManager.getTopicList().get(--topicIdToSubscribe).getName() + "'");
                    SubscriberDetails subscriber = new SubscriberDetails(socket.getInetAddress(), 8000);  // 8000 for all subscribers
                    List subscriberList = EventManager.getSubscriberMap().get(EventManager.getTopicList().get(topicIdToSubscribe));
                    subscriberList.add(subscriber);
                }
            } else if (input.equals("Subscribe by keyword")) {

                List<String> subscribedTopics = new ArrayList<>();
                String keyword = objectInStream.readUTF();
                SubscriberDetails subscriber = new SubscriberDetails(socket.getInetAddress(), 8000);  // 8000 for all subscribers
                for (Topic topic : EventManager.getSubscriberMap().keySet()) {
                    List<String> keywordList = topic.getKeywords();
                    if (keywordList.contains(keyword)) {
                        List subscriberList = EventManager.getSubscriberMap().get(topic);
                        subscriberList.add(subscriber);
                        subscribedTopics.add(topic.getName());
                    }
                }

                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(subscribedTopics);
                outObject.flush();
            } else if (input.equals("Un-subscriber")) {
                InetAddress remoteInetAddress = socket.getInetAddress();

                List<Topic> listToSend = new ArrayList<>();
                Iterator it = EventManager.getSubscriberMap().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    List<SubscriberDetails> list = (List<SubscriberDetails>) pair.getValue();
                    Iterator iter2 = list.iterator();
                    if (iter2.hasNext()) {
                        SubscriberDetails sub = (SubscriberDetails) iter2.next();
                        if (sub.getIpAddress().equals(remoteInetAddress)) {
                            listToSend.add((Topic) pair.getKey());
                        }
                    }
                }

                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(listToSend);
                outObject.flush();

                String topicToUnsubscribe = objectInStream.readUTF();

                List<SubscriberDetails> subscriberList = null;
                for (Topic t : EventManager.getSubscriberMap().keySet()) {
                    if (t.getName().equals(topicToUnsubscribe)) {
                        subscriberList = EventManager.getSubscriberMap().get(t);
                        Iterator iter = subscriberList.iterator();
                        while (iter.hasNext()) {
                            SubscriberDetails sub = (SubscriberDetails) iter.next();
                            if (sub.getIpAddress().equals(remoteInetAddress)) {
                                subscriberList.remove(sub);
                                break;
                            }
                        }
                    }
                }
            } else if (input.equals("Un-subscriber_All")) {

                InetAddress remoteInetAddress = socket.getInetAddress();

                List<SubscriberDetails> subscriberList = null;
                for (Topic t : EventManager.getSubscriberMap().keySet()) {
                    subscriberList = EventManager.getSubscriberMap().get(t);  // get value from Map
                    Iterator iter = subscriberList.iterator();          // for List - you can use iterator or for-each loop
                    while (iter.hasNext()) {
                        SubscriberDetails sub = (SubscriberDetails) iter.next();
                        if (sub.getIpAddress().equals(remoteInetAddress)) {
                            iter.remove();          // to avoid concurrent modification exception
                        }
                    }
                }
            } else if (input.equals("Subscribed list")) {
                InetAddress remoteInetAddress = socket.getInetAddress();

                List<Topic> listToSend = new ArrayList<>();
                Iterator it = EventManager.getSubscriberMap().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    List<SubscriberDetails> list = (List<SubscriberDetails>) pair.getValue();
                    Iterator iter2 = list.iterator();
                    if (iter2.hasNext()) {
                        SubscriberDetails sub = (SubscriberDetails) iter2.next();
                        if (sub.getIpAddress().equals(remoteInetAddress)) {
                            listToSend.add((Topic) pair.getKey());
                        }
                    }
                }

                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(listToSend);
                outObject.flush();

            }
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                EventManager.getBusyPorts().remove(currentPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     }
    }

    /*
     * update addresses of subscribers for newly published article
     */

    private void addAddresses(List<SubscriberDetails> list, Event article) {

        for(SubscriberDetails sub : list){
            if(!EventManager.getSubscribersToContact().containsKey(sub.getIpAddress())){
                List l = new ArrayList<>();
                l.add(article);
                EventManager.getSubscribersToContact().put(sub.getIpAddress(),l);
            }else {
                List l = EventManager.getSubscribersToContact().get(sub.getIpAddress());
                l.add(article);
            }
        }

    }

}
