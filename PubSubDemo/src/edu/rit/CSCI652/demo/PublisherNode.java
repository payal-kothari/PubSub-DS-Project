package edu.rit.CSCI652.demo;

import edu.rit.CSCI652.impl.PubSubAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by payalkothari on 9/7/17.
 */
public class PublisherNode {

    private static int topicId = 0;
    private static List<String> keywords = null;
    private static String topicName = null;

    public static void main(String[] args) throws IOException {
        init();
    }

    private static void init() throws IOException {
        System.out.println("Choose an option : ");
        System.out.println("1. Advertise a topic");
        System.out.println("2. Publish an article");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        if(option.equals("1")){
            topicId++;
            keywords = new ArrayList<>();
            System.out.println("Please enter keywords for the topic: ");
            System.out.println("Enter 'Over' when you are done: ");
            String keyword = scanner.nextLine();
            while(!keyword.equals("Over") && !keyword.equals("over")){
                System.out.println("Got : " + keyword);
                keywords.add(keyword);
                keyword = scanner.nextLine();
            }
            System.out.println("Please enter name of the topic: ");
            topicName = scanner.nextLine();
            createNewTopicAndAdvertise();

        }
    }

    private static void createNewTopicAndAdvertise() throws IOException {
        Topic topic = new Topic(topicId, keywords, topicName);
        new PubSubAgent().advertise(topic);
    }
}
