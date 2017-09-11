package edu.rit.CSCI652.impl;


import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EventManager implements Serializable{

	private static ServerSocket eventManagerSocket = null;
	private static List<Socket> socketList = null;
	private static DataInputStream inStream = null;
	private static ObjectInputStream objectInStream = null;
	private static HashSet<Topic> topicSet = null;


    /*
	 * Start the repo service
	 */
	private void startService() throws IOException, ClassNotFoundException {
		eventManagerSocket = new ServerSocket(9000);
		System.out.println("Event Manager started");
        socketList = new ArrayList<>();
        topicSet = new HashSet<>();

		while (true){
            Socket socket = eventManagerSocket.accept();
            socketList.add(socket);
            inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            if(inStream.readUTF().equals("Advertise")){
                objectInStream = new ObjectInputStream(socket.getInputStream());
                Topic topic = (Topic) objectInStream.readObject();
                addTopic(topic);
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
	private void addTopic(Topic topic) throws IOException {

        if(topicSet.add(topic)){
            System.out.println("New topic added");
        }else {
            System.out.println("Topic already exist");
        }

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
