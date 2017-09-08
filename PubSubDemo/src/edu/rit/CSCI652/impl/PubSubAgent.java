package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Publisher;
import edu.rit.CSCI652.demo.Subscriber;
import edu.rit.CSCI652.demo.Topic;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
	public void publish(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void advertise(Topic newTopic) throws IOException {
		// TODO Auto-generated method stub
		Socket advertiserSocket = new Socket("localhost", 9000);
        DataOutputStream out    = new DataOutputStream(advertiserSocket.getOutputStream());
        out.writeUTF("Advertise");
        ObjectOutputStream outObject = new ObjectOutputStream(advertiserSocket.getOutputStream());
        outObject.writeObject(newTopic);
	}


	
}
