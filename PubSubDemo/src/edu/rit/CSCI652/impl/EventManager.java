package edu.rit.CSCI652.impl;


import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager implements Serializable{

	private static ServerSocket eventManagerSocket = null;
	private static List<Socket> socketList = null;
	private static DataInputStream inStream = null;
	private static ObjectInputStream objectInStream = null;
	public static HashMap<Integer, Topic> topicMap =  new HashMap<Integer, Topic>();;
    public static HashMap<Integer, Event> eventMap = new HashMap<>();


    /*
	 * Start the repo service
	 */
	private void startService() throws IOException, ClassNotFoundException {
		eventManagerSocket = new ServerSocket(2000);
		System.out.println("Event Manager started");
        System.out.println();
        socketList = new ArrayList<>();
		while (true){
            Socket socket = eventManagerSocket.accept();
            socketList.add(socket);
            DataInputStream inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String input = inStream.readUTF();
            if(input.equals("Advertise")){
                System.out.println("Advertising a topic");
                new EventManagerAdvertise(socket).start();
            }else if(input.equals("Publish")){
                System.out.println("Publishing an article");
                new EventManagerPublish(socket).start();
            }
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
        topicMap.put( topic.id, topic);
        System.out.println("Topic - " + "'" + topic.name + "'" + " added");
        System.out.println();
    }

	/*
	 * add subscriber to the internal list
	 */
	private void addSubscriber(){
		
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


}
