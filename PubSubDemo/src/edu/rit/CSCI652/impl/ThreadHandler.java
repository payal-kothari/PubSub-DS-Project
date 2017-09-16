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

/**
 * Created by payalkothari on 9/11/17.
 */


public class ThreadHandler extends Thread implements Serializable{

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
            System.out.println("thread started ***********");
            ObjectInputStream objectInStream = new ObjectInputStream(socket.getInputStream());
            String input = objectInStream.readUTF();
            System.out.println("input got  ----- " + input);
            if(input.equals("Advertise")){
                System.out.println("Advertising a topic");
                Topic topic = (Topic) objectInStream.readObject();
                em.addTopic(topic);
            }else if(input.equals("Publish")){
                System.out.println("Publishing an article");
                Event article = (Event) objectInStream.readObject();                // event = article
                EventManager.eventMap.put(article.id, article);
                System.out.println("Article " +  "'" + article.title +"'"+ " added under topic name - " + "'" + article.topic.name + "'");
            }else if(input.equals("Subscriber")){
                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(EventManager.topicList);
                outObject.flush();
                System.out.println("Topic list sent");

                int topicIdToSubscribe = objectInStream.read();
                System.out.println("Subscribe to topic - '" + EventManager.topicList.get(--topicIdToSubscribe).name + "'");
                SubscriberDetails subscriber = new SubscriberDetails(socket.getInetAddress(), 8000);  // 8000 for all subscribers
                List subscriberList = EventManager.subscriberMap.get(EventManager.topicList.get(topicIdToSubscribe));
                subscriberList.add(subscriber);
               // EventManager.subscriberMap.put(EventManager.topicList.get(topicIdToSubscribe), list);
                System.out.println("Subscriber added");

            }else if(input.equals("Subscribe by keyword")){

                List<String> subscribedTopics = new ArrayList<>();
                String keyword = objectInStream.readUTF();
                System.out.println("in the block");
                SubscriberDetails subscriber = new SubscriberDetails(socket.getInetAddress(), 8000);  // 8000 for all subscribers
                for(Topic topic : EventManager.subscriberMap.keySet()){
                    List<String> keywordList = topic.keywords;
                    System.out.println("in the block 2");
                    if(keywordList.contains(keyword)){
                        System.out.println("keyword matched");
                        List subscriberList = EventManager.subscriberMap.get(topic);
                        subscriberList.add(subscriber);
                        subscribedTopics.add(topic.name);
                    }
                }

                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.writeObject(subscribedTopics);
                outObject.flush();
                System.out.println("Subscribed topics list sent");

            }else if(input.equals("Un-subscriber")){
                String topicToUnsubscribe = objectInStream.readUTF();
                InetAddress remoteInetAddress = socket.getInetAddress();

                List<SubscriberDetails> subscriberList = null;
                for(Topic t : EventManager.subscriberMap.keySet()){
                    if(t.name.equals(topicToUnsubscribe)){
                        System.out.println("topic name matched");
                        subscriberList = EventManager.subscriberMap.get(t);
                        Iterator iter = subscriberList.iterator();
                        while (iter.hasNext()){
                            SubscriberDetails sub = (SubscriberDetails) iter.next();
                            if(sub.ipAddress.equals(remoteInetAddress)){
                                subscriberList.remove(sub);
                                System.out.println("subscriber removed");
                                break;
                            }
                        }
                    }
                }
            }else if(input.equals("Un-subscriber_All")){

                InetAddress remoteInetAddress = socket.getInetAddress();

                List<SubscriberDetails> subscriberList = null;
                for(Topic t : EventManager.subscriberMap.keySet()){
                        subscriberList = EventManager.subscriberMap.get(t);  // get value from Map
                        Iterator iter = subscriberList.iterator();          // for List - you can use iterator or for-each loop
                        while (iter.hasNext()){
                            SubscriberDetails sub = (SubscriberDetails) iter.next();
                            if(sub.ipAddress.equals(remoteInetAddress)){
                                iter.remove();          // to avoid concurrent modification exception
                                System.out.println("subscriber removed");
                            }
                        }
                }
            }
        }catch (IOException e){

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        finally {
            try {
                socket.close();
                System.out.println("Connection on " + socket.getLocalPort() + " closed : " + socket.isClosed() + "\n" );
                EventManager.busyPorts.remove(currentPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
