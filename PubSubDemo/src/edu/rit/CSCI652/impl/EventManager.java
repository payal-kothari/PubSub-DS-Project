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
import java.util.concurrent.ConcurrentHashMap;

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
    private static int topidIndex;

    public int getTopidIndex() {
        return topidIndex;
    }

    public void setTopidIndex(int topidIndex) {
        this.topidIndex = topidIndex;
    }

    public static ConcurrentHashMap<InetAddress, List<Event>> getSubscribersToContact() {
        return subscribersToContact;
    }

    private static ConcurrentHashMap<InetAddress, List<Event>> subscribersToContact = new ConcurrentHashMap<>();

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
	private void startService(EventManager threadSyncObject) throws IOException, ClassNotFoundException {
		eventManagerSocket = new ServerSocket(2000);
		busyPorts.add(2000);
        new NotifyThreadHandler().start();    // new thread to send out notifications
		System.out.println("Event Manager started\n");
        socketList = new ArrayList<>();
		while (true){
            int nextFreePort = getNewFreePort();
            busyPorts.add(nextFreePort);
            ServerSocket subServerSocket = new ServerSocket(nextFreePort);
            origSocket = eventManagerSocket.accept();
            ObjectOutputStream outObject = new ObjectOutputStream(origSocket.getOutputStream());
            outObject.writeInt(nextFreePort);
            outObject.flush();
            origSocket.close();
            Socket subSocket = subServerSocket.accept();
            new ThreadHandler(subSocket, nextFreePort, threadSyncObject).start();  // new thread for new connection
        }
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
     * find new free port for new client
     */

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
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
        EventManager threadSyncObject = new EventManager();
		new EventManager().startService(threadSyncObject);
	}
}
