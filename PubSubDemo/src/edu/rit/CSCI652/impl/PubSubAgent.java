package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Publisher;
import edu.rit.CSCI652.demo.Subscriber;
import edu.rit.CSCI652.demo.Topic;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class PubSubAgent implements Publisher, Serializable{


	@Override
	public void publish(int eventId) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
        Socket publishSocket = new Socket("localhost", 2000);
        ObjectInputStream objectInStream = new ObjectInputStream(publishSocket.getInputStream());
        int reconnectPort = objectInStream.readInt();
        Socket reconnectSocket = new Socket("localhost", reconnectPort);
        System.out.println("Reconnected on port : " + reconnectSocket.getPort()); // gives remote m/c's port
        ObjectOutputStream outObject = new ObjectOutputStream(reconnectSocket.getOutputStream());
        outObject.writeUTF("Publish");
//        outObject.writeObject(event);
        outObject.flush();

        ObjectInputStream objectInStream2 = new ObjectInputStream(reconnectSocket.getInputStream());
        List<Topic> topicList = (List<Topic>) objectInStream2.readObject();
        Iterator iter = topicList.iterator();
        System.out.println("   **********  Topic list  **********  ");
        while (iter.hasNext()){
            Topic t = (Topic) iter.next();
            System.out.println(t.getId() + ". "+ t.getName());
        }

        Scanner scanner = new Scanner(System.in);
        if(!topicList.isEmpty()){
            System.out.println("Please enter the topic number  ");

            int topicId = scanner.nextInt();
            scanner.nextLine();

//            Topic selectedTopic = topicList.get(topicId);
            System.out.println("Please enter the title of the event: ");
            String eventTitle = scanner.nextLine();
            System.out.println("Please enter the content of the event: ");
            String eventContent = scanner.nextLine();

            outObject.write(eventId);
            outObject.write(topicId);
            outObject.writeUTF(eventTitle);
            outObject.writeUTF(eventContent);
            outObject.flush();
//            Event event = new Event(eventId, selectedTopic, title, content);
//            outObject.writeObject(event);

        }else{
            System.out.println(" No topics available ");
        }
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
