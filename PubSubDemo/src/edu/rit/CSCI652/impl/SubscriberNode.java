package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Subscriber;
import edu.rit.CSCI652.demo.Topic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by payalkothari on 9/15/17.
 */
public class SubscriberNode implements Subscriber, Serializable {

    private static List<String> subscribedTopics = new ArrayList<>();

    public static ServerSocket getNotificationListenSocket() {
        return notificationListenSocket;
    }

    private static ServerSocket notificationListenSocket;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        notificationListenSocket  = new ServerSocket(8000);
        init();
        notificationListenSocket.close();
    }

    private static void init() throws IOException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println();
            System.out.println("***** Choose an option : ");
            System.out.println("1. Subscribe");
            System.out.println("2. Subscribe by keyword");
            System.out.println("3. Un-subscribe");
            System.out.println("4. Un-subscribe to all the subscribe topics");
            System.out.println("5. Show list of all subscribed topics");

            int option = scanner.nextInt();
            scanner.nextLine();                 // need this after reading nextInt()
            if(option == 1 ){                   // subscribe
                Socket subscriberSocket = new Socket("localhost", 2000);
                System.out.println("\nConnection request sent on public port  2000");
                ObjectInputStream objectInStream = new ObjectInputStream(subscriberSocket.getInputStream());
                int reconnectPort = objectInStream.readInt();
                Socket reconnectSocket = new Socket("localhost", reconnectPort);
                System.out.println("Reconnected on port " + reconnectSocket.getPort() + " : " + reconnectSocket.isConnected()) ; // gives remote m/c's port

                new SubscriberThreadHandler().start();

                ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
                outObject.writeUTF("Subscriber");
                outObject.flush();

                ObjectInputStream objectInStream2 = new ObjectInputStream(reconnectSocket.getInputStream());
                System.out.println("\n " + reconnectSocket.getLocalPort() + " \n");
                List<Topic> topicList = (List<Topic>) objectInStream2.readObject();
                Iterator iter = topicList.iterator();

                if(!topicList.isEmpty()){
                    System.out.println("   **********  Topic list  **********  ");
                    boolean subscribedAll = true;
                    while (iter.hasNext()){
                        Topic t = (Topic) iter.next();
                        if(!subscribedTopics.isEmpty() && !subscribedTopics.contains(t.getName())){
                            subscribedAll = false;
                            System.out.println(t.getId() + ". "+ t.getName());
                        }else if(subscribedTopics.isEmpty()){
                            subscribedAll = false;
                            System.out.println(t.getId() + ". "+ t.getName());
                        }
                    }

                    if(!topicList.isEmpty() && subscribedAll == false){
                        System.out.println("Please enter the topic number you want to subscribe to ");

                        int topicId = scanner.nextInt();
                        scanner.nextLine();
                        outObject.write(topicId);
                        outObject.flush();

                        Iterator iter2 = topicList.iterator();
                        while (iter2.hasNext()){
                            Topic t = (Topic) iter2.next();
                            if(t.getId() == topicId){
                                subscribedTopics.add(t.getName());
                            }
                        }
                    }else if(subscribedAll == true){
                        System.out.println(" you have subscribed all topics");
                    }
                }else {
                    System.out.println("No topics available");
                }



            }else if(option == 2){
                Socket subscriberSocket = new Socket("localhost", 2000);
                System.out.println("\n Connection request sent on public port  2000");
                ObjectInputStream objectInStream = new ObjectInputStream(subscriberSocket.getInputStream());
                int reconnectPort = objectInStream.readInt();
                Socket reconnectSocket = new Socket("localhost", reconnectPort);
                System.out.println("Reconnected on port " + reconnectSocket.getPort() + " : " + reconnectSocket.isConnected()) ; // gives remote m/c's port

                new SubscriberThreadHandler().start();

                ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
                outObject.writeUTF("Subscribe by keyword");
                outObject.flush();

                System.out.println("Please enter a keyword : ");
                String keyword = scanner.nextLine();
                outObject.writeUTF(keyword);
                outObject.flush();

                ObjectInputStream objectInStream2 = new ObjectInputStream(reconnectSocket.getInputStream());
                List<String> topicList = (List<String>) objectInStream2.readObject();
                subscribedTopics.addAll(topicList);
                System.out.println("You have subscribed to following topics :");
                int index = 0;
                for(String topic : topicList){
                    System.out.println(++index + ". " + topic);
                }

            }else if(option == 3){
                Socket subscriberSocket = new Socket("localhost", 2000);
                System.out.println("\n Connection request sent on public port  2000");
                ObjectInputStream objectInStream = new ObjectInputStream(subscriberSocket.getInputStream());
                int reconnectPort = objectInStream.readInt();
                Socket reconnectSocket = new Socket("localhost", reconnectPort);
                System.out.println("Reconnected on port " + reconnectSocket.getPort() + " : " + reconnectSocket.isConnected()) ; // gives remote m/c's port

                new SubscriberThreadHandler().start();

                ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
                outObject.writeUTF("Un-subscriber");
                outObject.flush();

                System.out.println(" ********  Subscribed topics ********");
                Iterator iter = subscribedTopics.iterator();
                int index = 0;
                while (iter.hasNext()){
                    System.out.println(++index + ". " + iter.next());
                }

                if(!subscribedTopics.isEmpty()){
                    System.out.println("Please enter topic number you want to un-subscribe");
                    int unSubscribeId = scanner.nextInt();
                    scanner.nextLine();

                    String topicToUnsubscribe = subscribedTopics.get(--unSubscribeId);
                    subscribedTopics.remove(unSubscribeId);

                    outObject.writeUTF(topicToUnsubscribe);
                    outObject.flush();
                }else {
                    System.out.println("\n You have not subscribed to any topics ");
                }
            }else if(option == 4){
                Socket subscriberSocket = new Socket("localhost", 2000);
                System.out.println("\nConnection request sent on public port  2000");
                ObjectInputStream objectInStream = new ObjectInputStream(subscriberSocket.getInputStream());
                int reconnectPort = objectInStream.readInt();
                Socket reconnectSocket = new Socket("localhost", reconnectPort);
                System.out.println("Reconnected on port " + reconnectSocket.getPort() + " : " + reconnectSocket.isConnected()) ; // gives remote m/c's port

                new SubscriberThreadHandler().start();

                ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
                outObject.writeUTF("Un-subscriber_All");
                outObject.flush();

                if(!subscribedTopics.isEmpty()){
                    subscribedTopics.clear();
                }else {
                    System.out.println(" You have not subscribed to any topics ");
                }
            }else if(option == 5){
                int index = 0;
                if(!subscribedTopics.isEmpty()){
                    System.out.println("\n ********  You have subscribed to the following topics ********");
                    for(String topic : subscribedTopics){
                        System.out.println(++index + ". "  +topic);
                    }
                }else {
                    System.out.println("\n ********  You have not subscribed to any topics");
                }

            }
        }



    }

    @Override
    public void subscribe(Topic topic) {

    }

    @Override
    public void subscribe(String keyword) {

    }

    @Override
    public void unsubscribe(Topic topic) {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void listSubscribedTopics() {

    }
}