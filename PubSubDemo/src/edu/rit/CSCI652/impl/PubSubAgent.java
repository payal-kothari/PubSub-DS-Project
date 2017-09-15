package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Publisher;
import edu.rit.CSCI652.demo.Subscriber;
import edu.rit.CSCI652.demo.Topic;

import java.io.*;
import java.net.Socket;

public class PubSubAgent implements Publisher, Subscriber, Serializable{

	@Override
	public void subscribe(Topic topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String keyword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(Topic topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listSubscribedTopics() {
		// TODO Auto-generated method stub
	}

	@Override
	public void publish(Event event) throws IOException {
		// TODO Auto-generated method stub
        Socket publishSocket = new Socket("localhost", 2000);
        ObjectInputStream objectInStream = new ObjectInputStream(publishSocket.getInputStream());
        int reconnectPort = objectInStream.readInt();
        Socket reconnectSocket = new Socket("localhost", reconnectPort);
        System.out.println("Reconnected on port : " + reconnectSocket.getPort()); // gives remote m/c's port
        ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
        outObject.writeUTF("Publish");
        outObject.writeObject(event);
        outObject.flush();
    }

	@Override
	public void advertise(Topic newTopic) throws IOException {
		// TODO Auto-generated method stub
		Socket advertiserSocket = new Socket("localhost", 2000);
        System.out.println("\nConnection request sent on public port  2000");
        ObjectInputStream objectInStream = new ObjectInputStream(advertiserSocket.getInputStream());
        int reconnectPort = objectInStream.readInt();
        Socket reconnectSocket = new Socket("localhost", reconnectPort);
        System.out.println("Reconnected on port " + reconnectSocket.getPort() + " : " + reconnectSocket.isConnected()) ; // gives remote m/c's port
        ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
        outObject.writeUTF("Advertise");
        outObject.writeObject(newTopic);
        outObject.flush();
		System.out.println("Topic successfully advetised");
	}


	
}
