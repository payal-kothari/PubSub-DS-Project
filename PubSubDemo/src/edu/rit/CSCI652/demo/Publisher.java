package edu.rit.CSCI652.demo;

import java.io.IOException;

public interface Publisher {
	/*
	 * publish an event of a specific topic with title and content
	 */
	public void publish(Event event);
	
	/*
	 * advertise new topic
	 */
	public void advertise(Topic newTopic) throws IOException;
}
