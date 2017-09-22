package edu.rit.CSCI652.impl;


import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// Test cases

// 1) add a topic and advertise
// 2) one or more subscriber subscribe
// 3) time de-coupling : bring down one og the subscriber , use a publisher to publish , and you must notify a subscriber when it comes online
// comments
// create multiple dockers, check they can talk to each other

public class EventManager implements Serializable {

	private static ServerSocket eventManagerSocket = null;
	private static List<Socket> socketList = null;
    private static HashMap<Integer, Topic> topicMap =  new HashMap<>();
    private static List<Topic> topicList= new ArrayList<>();
    private static HashMap<Integer, Event> eventMap = new HashMap<>();
    private static HashSet<Integer> busyPorts = new HashSet<>();
    private static HashMap<Topic, List<SubscriberDetails>> subscriberMap = new HashMap<>();
    private static Socket origSocket;

    public static HashMap<InetAddress, List<Event>> getSubscribersToContact() {
        return subscribersToContact;
    }

    private static HashMap<InetAddress, List<Event>> subscribersToContact = new HashMap<>();

    public static List<Topic> getTopicList() {
        return topicList;
    }

    public static HashMap<Integer, Event> getEventMap() {
        return eventMap;
    }

    public static HashSet<Integer> getBusyPorts() {
        return busyPorts;
    }

    public static HashMap<Topic, List<SubscriberDetails>> getSubscriberMap() {
        return subscriberMap;
    }

    /*
	 * Start the repo service
	 */
	private void startService() throws IOException, ClassNotFoundException {
		eventManagerSocket = new ServerSocket(2000);
		busyPorts.add(2000);
        new NotifyThreadHandler().start();
		System.out.println("Event Manager started\n");
        socketList = new ArrayList<>();
		while (true){
			// Server can accepts connections from two different clients at the same time on the same port but
			// both clients can not talk at the same time on that same port. So, connect two different clients
			// on different ports of the server to make the system scalable.

            // randomize the ports for security .. ex. hashing
            int nextFreePort = getNewFreePort();
            busyPorts.add(nextFreePort);
            ServerSocket subServerSocket = new ServerSocket(nextFreePort);
            origSocket = eventManagerSocket.accept();
            ObjectOutputStream outObject = new ObjectOutputStream(origSocket.getOutputStream());
            outObject.writeInt(nextFreePort);
            outObject.flush();
            origSocket.close();
            Socket subSocket = subServerSocket.accept();
            new ThreadHandler(subSocket, nextFreePort).start();
        }
	}



    /*
     * notify all subscribers of new event
     */
	private void notifySubscribers(Event event) {
		
	}
	
	/*
	 * add new topic when received advertisement of new topic
	 */
	public void addTopic(Topic topic) throws IOException {
        topicMap.put( topic.getId(), topic);
        topicList.add(topic);
        if(!subscriberMap.containsKey(topic)){
            subscriberMap.put(topic, new ArrayList<SubscriberDetails>());
        }
        System.out.println("Topic - " + "'" + topic.getName() + "'" + " added");
    }

	/*
	 * add subscriber to the internal list
	 */
	private void addSubscriber(){               // done

	}
	
	/*
	 * remove subscriber from the list
	 */
	private void removeSubscriber(){
		
	}
	
	/*
	 * show the list of subscriber for a specified topic
	 */
	private void showSubscribers(Topic topic){
		
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		new EventManager().startService();
	}


    public int getNewFreePort() {
        Random random = new Random();
        int max = 65535;  // total number of ports
        int min = 1024;   // 0-1023 are reserved ports , so starting with 1024
        int newPort = 2000;

        while(busyPorts.contains(newPort)){
            newPort = random.nextInt(max - min) + min;   // inclusive of min and max
        }
        return newPort;
    }
}
