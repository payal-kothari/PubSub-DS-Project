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
	public void publish(Event event) throws IOException {
		// TODO Auto-generated method stub
        Socket publishSocket = new Socket("localhost", 2000);
        DataOutputStream out = new DataOutputStream(publishSocket.getOutputStream());
        out.writeUTF("Publish");
        ObjectOutputStream outObject = new ObjectOutputStream(publishSocket.getOutputStream());
        outObject.writeObject(event);
    }

	@Override
	public void advertise(Topic newTopic) throws IOException {
		// TODO Auto-generated method stub
		Socket advertiserSocket = new Socket("localhost", 2000);
        ObjectOutputStream outObject = new ObjectOutputStream(advertiserSocket.getOutputStream());
        outObject.writeUTF("Advertise");
        outObject.writeObject(newTopic);
        outObject.flush();
	}


	
}
