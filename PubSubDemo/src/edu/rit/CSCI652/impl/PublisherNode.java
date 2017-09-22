package edu.rit.CSCI652.impl;

import edu.rit.CSCI652.demo.Event;
import edu.rit.CSCI652.demo.Topic;

import java.io.IOException;
import java.util.*;

/**
 * Created by payalkothari on 9/7/17.
 */
public class PublisherNode {

    private static int topicId = 0;
    private static int eventId = 0;
    private static List<String> keywords = null;
    private static String topicName = null;
    private static HashMap<Integer, Topic> allTopicsMap =  new HashMap<Integer, Topic>();


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        init();
    }

    private static void init() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println();
            System.out.println("***** Choose an option : ");
            System.out.println("1. Advertise a topic");
            System.out.println("2. Publish an article");
            int option = scanner.nextInt();
            scanner.nextLine();                 // need this after reading nextInt()
            if(option == 1 ){                 // Advertising a new topic
                topicId++;
                keywords = new ArrayList<>();
                System.out.println("Please enter name of the topic: ");
                topicName = scanner.nextLine();
                System.out.println("Please enter keywords(comma separated) for the topic: ");
                String keyword = scanner.nextLine();
                keywords = Arrays.asList(keyword.split(","));
                createNewTopicAndAdvertise();
            }else if(option == 2){
                eventId++;
                new PubSubAgent().publish(eventId);
            }
        }
    }

    private static void createNewTopicAndAdvertise() throws IOException {
        Topic topic = new Topic(topicId, keywords, topicName);
        allTopicsMap.put(topic.getId(), topic);
        new PubSubAgent().advertise(topic);
    }
}
